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
package com.infinities.keystone4j.token.provider.api;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.action.AbstractAuthAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.auth.TokenIdAndDataV2;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.notification.Actions;
import com.infinities.keystone4j.notification.NotificationCallback;
import com.infinities.keystone4j.notification.Notifications;
import com.infinities.keystone4j.notification.Notifications.Payload;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.token.provider.api.command.IssueV2TokenCommand;
import com.infinities.keystone4j.token.provider.api.command.IssueV3TokenCommand;
import com.infinities.keystone4j.token.provider.api.command.ListRevokedTokensCommand;
import com.infinities.keystone4j.token.provider.api.command.RevokeTokenCommand;
import com.infinities.keystone4j.token.provider.api.command.ValidateTokenCommand;
import com.infinities.keystone4j.token.provider.api.command.ValidateV2TokenCommand;
import com.infinities.keystone4j.token.provider.api.command.ValidateV3TokenCommand;
import com.infinities.keystone4j.token.provider.driver.BaseProvider;
import com.infinities.keystone4j.token.provider.driver.PkiProvider;
import com.infinities.keystone4j.token.provider.driver.PkizProvider;
import com.infinities.keystone4j.token.provider.driver.UuidProvider;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.Cms.Algorithm;

public class TokenProviderApiImpl implements TokenProviderApi {

	private final static Logger logger = LoggerFactory.getLogger(TokenProviderApiImpl.class);
	private final TokenProviderDriver driver;
	private final RevokeApi revokeApi;

	private final AssignmentApi assignmentApi;
	// private final CatalogApi catalogApi;
	// private final IdentityApi identityApi;
	private final TrustApi trustApi;

	private final PersistenceManager persistenceManager;
	private final BaseProvider PKI_PROVIDER;
	private final BaseProvider PKIZ_PROVIDER;
	private final BaseProvider UUID_PROVIDER;
	private final Map<String, BaseProvider> FORMAT_TO_PROVIDER;


	public TokenProviderApiImpl(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TrustApi trustApi, RevokeApi revokeApi, TokenProviderDriver tokenProviderDriver,
			PersistenceManager persistenceManager) throws Exception {
		super();
		this.persistenceManager = persistenceManager;
		persistenceManager.setTokenProviderApi(this);
		this.revokeApi = revokeApi;
		this.trustApi = trustApi;
		this.assignmentApi = assignmentApi;
		PKI_PROVIDER = new PkiProvider(identityApi, assignmentApi, catalogApi, trustApi);
		PKIZ_PROVIDER = new PkizProvider(identityApi, assignmentApi, catalogApi, trustApi);
		UUID_PROVIDER = new UuidProvider(identityApi, assignmentApi, catalogApi, trustApi);
		Map<String, BaseProvider> m = new HashMap<String, BaseProvider>();
		m.put("PKI", PKI_PROVIDER);
		m.put("PKIZ", PKIZ_PROVIDER);
		m.put("UUID", UUID_PROVIDER);
		FORMAT_TO_PROVIDER = Collections.unmodifiableMap(m);
		this.driver = getTokenProvider();
		registerCallbackListeners();
	}

	private void registerCallbackListeners() {
		Map<Actions, List<Entry<String, ? extends NotificationCallback>>> callbacks = new HashMap<Actions, List<Entry<String, ? extends NotificationCallback>>>();
		List<Entry<String, ? extends NotificationCallback>> deletedList = new ArrayList<Entry<String, ? extends NotificationCallback>>();
		deletedList.add(Maps.immutableEntry("OS-TRUST:trust", new TrustDeletedEventCallback()));
		deletedList.add(Maps.immutableEntry("user", new DeleteUserTokensCallback()));
		deletedList.add(Maps.immutableEntry("domain", new DeleteDomainTokensCallback()));
		callbacks.put(Actions.deleted, deletedList);

		List<Entry<String, ? extends NotificationCallback>> disabledList = new ArrayList<Entry<String, ? extends NotificationCallback>>();
		disabledList.add(Maps.immutableEntry("user", new DeleteUserTokensCallback()));
		disabledList.add(Maps.immutableEntry("domain", new DeleteDomainTokensCallback()));
		disabledList.add(Maps.immutableEntry("project", new DeleteProjectTokensCallback()));
		callbacks.put(Actions.disabled, disabledList);

		List<Entry<String, ? extends NotificationCallback>> internalList = new ArrayList<Entry<String, ? extends NotificationCallback>>();
		disabledList.add(Maps
				.immutableEntry(Notifications.INVALIDATE_USER_TOKEN_PERSISTENCE, new DeleteUserTokensCallback()));
		disabledList.add(Maps.immutableEntry(Notifications.INVALIDATE_USER_PROJECT_TOKEN_PERSISTENCE,
				new DeleteUserProjectTokensCallback()));
		disabledList.add(Maps.immutableEntry(Notifications.INVALIDATE_USER_OAUTH_CONSUMER_TOKENS,
				new DeleteUserOauthConsumerTokensCallback()));
		callbacks.put(Actions.internal, internalList);

		for (Entry<Actions, List<Entry<String, ? extends NotificationCallback>>> entry : callbacks.entrySet()) {
			Actions event = entry.getKey();
			List<Entry<String, ? extends NotificationCallback>> cbInfos = entry.getValue();
			for (Entry<String, ? extends NotificationCallback> cbInfo : cbInfos) {
				String resourceType = cbInfo.getKey();
				NotificationCallback callbackFns = cbInfo.getValue();
				Notifications.registerEventCallback(event, resourceType, callbackFns);
			}
		}
	}

	private BaseProvider getTokenProvider() {
		if (Config.Instance.getOpt(Config.Type.signing, "token_format") != null
				&& !Strings.isNullOrEmpty(Config.Instance.getOpt(Config.Type.signing, "token_format").asText())) {
			logger.warn("[signing] token_format is deprecated. Please change to setting the [token] provider configuration value instead");
			try {

				BaseProvider mapped = FORMAT_TO_PROVIDER.get(Config.Instance.getOpt(Config.Type.signing, "token_format")
						.asText());
				return mapped;
			} catch (Exception e) {
				throw Exceptions.UnexpectedException
						.getInstance("Unrecognized keystone.conf [signing] token_format: expected either \'UUID\' or \'PKI\'");
			}
		}

		if (Config.Instance.getOpt(Config.Type.token, "provider") == null) {
			return UUID_PROVIDER;
		} else {
			String provider = Config.Instance.getOpt(Config.Type.token, "provider").asText();
			logger.debug("provider: {}", provider);
			String[] splits = provider.split("\\.");
			String providerStr = splits[splits.length - 1];
			providerStr = providerStr.replace("Provider", "");
			return FORMAT_TO_PROVIDER.get(providerStr.toUpperCase());
		}

	}

	@Override
	public String getUniqueId(String tokenId) throws UnsupportedEncodingException, NoSuchAlgorithmException,
			DecoderException {
		return Cms.Instance.hashToken(tokenId,
				Algorithm.valueOf(Config.Instance.getOpt(Config.Type.token, "hash_algorithm").asText()));
	}

	@Override
	public ITokenDataWrapper validateToken(String tokenid, String belongsTo) throws Exception {
		ValidateTokenCommand command = new ValidateTokenCommand(this, revokeApi, driver, persistenceManager, tokenid,
				belongsTo);
		return command.execute();
	}

	@Override
	public TokenV2DataWrapper validateV2Token(String tokenid, String belongsTo) throws Exception {
		ValidateV2TokenCommand command = new ValidateV2TokenCommand(this, revokeApi, driver, persistenceManager, tokenid,
				belongsTo);
		return command.execute();
	}

	@Override
	public TokenDataWrapper validateV3Token(String tokenid) throws Exception {
		ValidateV3TokenCommand command = new ValidateV3TokenCommand(this, revokeApi, driver, persistenceManager, tokenid);
		return command.execute();
	}

	@Override
	public TokenIdAndDataV2 issueV2Token(AuthTokenData authTokenData, List<Role> rolesRef,
			Map<String, Map<String, Map<String, String>>> catalogRef) throws Exception {
		IssueV2TokenCommand command = new IssueV2TokenCommand(this, revokeApi, driver, persistenceManager, authTokenData,
				rolesRef, catalogRef);
		return command.execute();
	}

	@Override
	public TokenIdAndData issueV3Token(String userid, List<String> methodNames, Calendar expiresAt, String projectid,
			String domainid, AbstractAuthAction.AuthContext authContext, Trust trust, Metadata metadataRef,
			boolean includeCatalog, String parentAuditid) throws Exception {
		IssueV3TokenCommand command = new IssueV3TokenCommand(this, revokeApi, driver, persistenceManager, userid,
				methodNames, expiresAt, projectid, domainid, authContext, trust, metadataRef, includeCatalog, parentAuditid);
		return command.execute();
	}

	@Override
	public void revokeToken(String tokenid, boolean revokeChain) throws Exception {
		RevokeTokenCommand command = new RevokeTokenCommand(this, revokeApi, driver, persistenceManager, tokenid,
				revokeChain);
		command.execute();
	}

	@Override
	public List<Token> listRevokedTokens() throws Exception {
		ListRevokedTokensCommand command = new ListRevokedTokensCommand(this, revokeApi, driver, persistenceManager);
		return command.execute();
	}

	protected PersistenceManager getPersistence() {
		return this.persistenceManager;
	}


	public class TrustDeletedEventCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) throws Exception {
			if (Config.Instance.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
				String trustId = (String) payload.getResourceInfo();
				Trust trust = trustApi.getTrust(trustId, true);
				getPersistence().deleteTokens(trust.getTrustorUserId(), null, trustId, null);
			}
		}

		// @Override
		// public String getName() {
		// return "_trust_deleted_event_callback";
		// }
	}

	public class DeleteUserTokensCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) throws Exception {
			if (Config.Instance.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
				String userId = (String) payload.getResourceInfo();
				getPersistence().deleteTokensForUser(userId, null);
			}
		}

	}

	public class DeleteDomainTokensCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) throws Exception {
			if (Config.Instance.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
				String domainId = (String) payload.getResourceInfo();
				getPersistence().deleteTokensForDomain(domainId);
			}
		}

	}

	public class DeleteUserProjectTokensCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) throws Exception {
			if (Config.Instance.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
				String userId = ((com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand.Payload) payload
						.getResourceInfo()).getUserid();
				String projectId = ((com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand.Payload) payload
						.getResourceInfo()).getProjectid();
				getPersistence().deleteTokensForUser(userId, projectId);
			}
		}

	}

	public class DeleteProjectTokensCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) throws Exception {
			if (Config.Instance.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
				String projectId = (String) payload.getResourceInfo();
				getPersistence().deleteTokensForUsers(assignmentApi.listUserIdsForProject(projectId), projectId);
			}
		}

	}

	public class DeleteUserOauthConsumerTokensCallback implements NotificationCallback {

		@Override
		public void invoke(String service, String resourceType, Actions operation, Payload payload) throws Exception {
			if (Config.Instance.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
				String userId = ((UserIdAndConsumerId) payload.getResourceInfo()).getUserId();
				String consumerId = ((UserIdAndConsumerId) payload.getResourceInfo()).getConsumerId();
				getPersistence().deleteTokens(userId, null, null, consumerId);
			}
		}


		public class UserIdAndConsumerId {

			private String userId;
			private String consumerId;


			public String getUserId() {
				return userId;
			}

			public void setUserId(String userId) {
				this.userId = userId;
			}

			public String getConsumerId() {
				return consumerId;
			}

			public void setConsumerId(String consumerId) {
				this.consumerId = consumerId;
			}

		}

	}

	// @Override
	// public void checkV3Token(String tokenid) {
	// CheckV3TokenCommand command = new CheckV3TokenCommand(this,
	// tokenProviderDriver, tokenid);
	// command.execute();
	// }

	// @Override
	// public TokenV2DataWrapper checkV2Token(String tokenid, String belongsTo)
	// {
	// CheckV2TokenCommand command = new CheckV2TokenCommand(this,
	// tokenProviderDriver, tokenid, belongsTo);
	// return command.execute();
	// }

}
