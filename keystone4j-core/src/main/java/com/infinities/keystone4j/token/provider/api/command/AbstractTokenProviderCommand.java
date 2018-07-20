/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.token.provider.api.command;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.contrib.revoke.model.Model;
import com.infinities.keystone4j.contrib.revoke.model.Model.TokenValues;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Bind;
import com.infinities.keystone4j.model.token.IToken;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenData;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.TokenV2;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public abstract class AbstractTokenProviderCommand {

	private final static Logger logger = LoggerFactory.getLogger(AbstractTokenProviderCommand.class);
	private final TokenProviderApi tokenProviderApi;
	private final TokenProviderDriver tokenProviderDriver;
	private final RevokeApi revokeApi;
	// private final static String UNEXPECTED_ERROR =
	// "Unexpected error or malformed token determining token expiry: {}";
	// private final static String FAILED_TO_VALIDATE_TOKEN =
	// "Failed to validate token";
	public final String V2 = KeystoneToken.V2;
	public final String V3 = KeystoneToken.V3;
	private final PersistenceManager persistenceManager;


	public AbstractTokenProviderCommand(TokenProviderApi tokenProviderApi, RevokeApi revokeApi,
			TokenProviderDriver tokenProviderDriver, PersistenceManager persistenceManager) {
		super();
		this.revokeApi = revokeApi;
		this.tokenProviderApi = tokenProviderApi;
		this.tokenProviderDriver = tokenProviderDriver;
		this.persistenceManager = persistenceManager;
	}

	public TokenProviderApi getTokenProviderApi() {
		return tokenProviderApi;
	}

	public TokenProviderDriver getTokenProviderDriver() {
		return tokenProviderDriver;
	}

	public RevokeApi getRevokeApi() {
		return revokeApi;
	}

	public String getUniqueId(String tokenid) throws Exception {
		return this.getTokenProviderApi().getUniqueId(tokenid);
	}

	public void isValidToken(ITokenDataWrapper token) throws Exception {
		if (token instanceof TokenV2DataWrapper) {
			isValidToken((TokenV2DataWrapper) token);
			return;
		} else if (token instanceof TokenDataWrapper) {
			isValidToken((TokenDataWrapper) token);
			return;
		}
		logger.error("malformed token");
		throw Exceptions.TokenNotFoundException.getInstance("Failed to validate token");
	}

	protected void checkRevocation(IToken token) throws Exception {
		String version = this.getTokenProviderDriver().getTokenVersion(token);
		if (V3.equals(version)) {
			checkRevocationV3((TokenDataWrapper) token);
		} else if (V2.equals(version)) {
			checkRevocationV2((TokenV2DataWrapper) token);
		}
	}

	protected void checkRevocationV2(TokenV2DataWrapper token) throws Exception {
		Access tokenData = null;
		try {
			tokenData = token.getAccess();
		} catch (Exception e) {
			throw Exceptions.TokenNotFoundException.getInstance("Failed to validate token");
		}

		if (revokeApi != null) {
			TokenValues tokenValues = Model.buildTokenValuesV2(tokenData,
					Config.getOpt(Config.Type.identity, "default_domain_id").asText());
			revokeApi.checkToken(tokenValues);
		}
	}

	private void checkRevocationV3(TokenDataWrapper token) throws Exception {
		TokenData tokenData = null;
		try {
			tokenData = token.getToken();
		} catch (Exception e) {
			throw Exceptions.TokenNotFoundException.getInstance("Failed to validate token");
		}

		if (revokeApi != null) {
			TokenValues tokenValues = Model.buildTokenValues(tokenData);
			revokeApi.checkToken(tokenValues);
		}
	}

	protected ITokenDataWrapper validateToken(String tokenid) throws Exception {
		Token tokenRef = this.getPersistence().getToken(tokenid);
		String version = this.getTokenProviderDriver().getTokenVersion(tokenRef);
		if (V3.equals(version)) {
			return this.getTokenProviderDriver().validateV3Token(tokenRef);
		} else if (V2.equals(version)) {
			return this.getTokenProviderDriver().validateV2Token(tokenRef);
		}

		throw Exceptions.UnsupportedTokenVersionException.getInstance();
	}

	protected void isValidToken(TokenV2DataWrapper token) throws Exception {
		Calendar currentTime = Calendar.getInstance();
		Calendar expiry = null;
		try {
			Access tokenData = token.getAccess();
			expiry = tokenData.getToken().getExpires();
		} catch (Exception e) {
			logger.error("Unexpected error or malformed token determining token expiry: :s", token);
			throw Exceptions.TokenNotFoundException.getInstance("Failed to validate token");
		}

		if (currentTime.before(expiry)) {
			checkRevocationV2(token);
			return;
		} else {
			throw Exceptions.TokenNotFoundException.getInstance("Failed to validate token");
		}
	}

	protected void isValidToken(TokenDataWrapper token) throws Exception {
		Calendar currentTime = Calendar.getInstance();
		Calendar expiry = null;
		try {
			TokenData tokenData = token.getToken();
			expiry = tokenData.getExpireAt();

			if (expiry == null) {
				expiry = tokenData.getToken().getExpires();
			}
		} catch (Exception e) {
			logger.error("Unexpected error or malformed token determining token expiry: :s", token);
			throw Exceptions.TokenNotFoundException.getInstance("Failed to validate token");
		}

		if (currentTime.before(expiry)) {
			checkRevocationV3(token);
			return;
		} else {
			throw Exceptions.TokenNotFoundException.getInstance("Failed to validate token");
		}
	}

	public void tokenBelongsTo(ITokenDataWrapper token, String belongsTo) {
		if (!Strings.isNullOrEmpty(belongsTo)) {
			TokenV2 tokenData = ((TokenV2DataWrapper) token).getAccess().getToken();
			if (tokenData.getTenant() == null || !belongsTo.equals(tokenData.getTenant().getId())) {
				throw Exceptions.UnauthorizedException.getInstance();
			}
		}
	}

	protected void createToken(String tokenid, Data tokenData) throws Exception {
		try {
			getPersistence().createToken(tokenid, tokenData);
		} catch (Exception e) {
			logger.warn("create token failed: {}", e);
			try {
				getPersistence().getToken(tokenid);
			} catch (Exception ex) {
				throw ex;
			}
		}
	}

	protected PersistenceManager getPersistence() {
		return this.persistenceManager;
	}


	public static class Data {

		private String key;
		private String id;
		private Calendar expires;
		private User user;
		private Project tenant;
		private Metadata metadata;
		private ITokenDataWrapper tokenData;
		private Bind bind;
		private String trustid;
		private String tokenVersion;


		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Calendar getExpires() {
			return expires;
		}

		public void setExpires(Calendar expires) {
			this.expires = expires;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Project getTenant() {
			return tenant;
		}

		public void setTenant(Project tenant) {
			this.tenant = tenant;
		}

		public Metadata getMetadata() {
			return metadata;
		}

		public void setMetadata(Metadata metadata) {
			this.metadata = metadata;
		}

		public ITokenDataWrapper getTokenData() {
			return tokenData;
		}

		public void setTokenData(ITokenDataWrapper tokenData) {
			this.tokenData = tokenData;
		}

		public Bind getBind() {
			return bind;
		}

		public void setBind(Bind bind) {
			this.bind = bind;
		}

		public String getTrustid() {
			return trustid;
		}

		public void setTrustid(String trustid) {
			this.trustid = trustid;
		}

		public String getTokenVersion() {
			return tokenVersion;
		}

		public void setTokenVersion(String tokenVersion) {
			this.tokenVersion = tokenVersion;
		}

	}
}
