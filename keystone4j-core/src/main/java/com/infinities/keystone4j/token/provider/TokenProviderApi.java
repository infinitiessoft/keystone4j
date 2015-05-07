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
package com.infinities.keystone4j.token.provider;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.DecoderException;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.auth.controller.action.AbstractAuthAction;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.auth.TokenIdAndDataV2;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Bind;
import com.infinities.keystone4j.model.token.IToken;
import com.infinities.keystone4j.model.token.Metadata;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.model.KeystoneToken;

public interface TokenProviderApi extends Api {

	public static Set<String> VERSIONS = KeystoneToken.VERSIONS;
	public static String V2 = KeystoneToken.V2;
	public static String V3 = KeystoneToken.V3;


	// IToken validToken(String tokenid);

	TokenIdAndData issueV3Token(String userid, List<String> methodNames, Calendar expiresAt, String projectid,
			String domainid, AbstractAuthAction.AuthContext authContext, Trust trust, Metadata metadataRef,
			boolean includeCatalog, String parentAuditid) throws Exception;

	// @Deprecated
	// void checkV3Token(String tokenid);

	// revokeChain =false
	void revokeToken(String tokenid, boolean revokeChain) throws Exception;

	TokenDataWrapper validateV3Token(String tokenid) throws Exception;

	TokenIdAndDataV2 issueV2Token(AuthTokenData authTokenData, List<Role> rolesRef,
			Map<String, Map<String, Map<String, String>>> catalogRef) throws Exception, DecoderException;

	TokenV2DataWrapper validateV2Token(String tokenid, String belongsTo) throws Exception;

	// @Deprecated
	// TokenV2DataWrapper checkV2Token(String tokenid, String belongsTo);

	List<Token> listRevokedTokens() throws Exception;

	// belongsTo = null
	ITokenDataWrapper validateToken(String tokenid, String belongsTo) throws Exception;

	String getUniqueId(String tokenId) throws Exception;


	public static class AuthTokenData implements IToken {

		private String id;
		private User user;
		private Project tenant;
		private Metadata metadata;
		private Calendar expires;
		private String parentAuditId;
		private Bind bind;


		@Override
		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		@Override
		public Project getTenant() {
			return tenant;
		}

		public void setTenant(Project tenant) {
			this.tenant = tenant;
		}

		@Override
		public Metadata getMetadata() {
			return metadata;
		}

		public void setMetadata(Metadata metadata) {
			this.metadata = metadata;
		}

		@Override
		public Calendar getExpires() {
			return expires;
		}

		public void setExpires(Calendar expires) {
			this.expires = expires;
		}

		@Override
		public String getParentAuditId() {
			return parentAuditId;
		}

		public void setParentAuditId(String parentAuditId) {
			this.parentAuditId = parentAuditId;
		}

		@Override
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@Override
		public Bind getBind() {
			return bind;
		}

		public void setBind(Bind bind) {
			this.bind = bind;
		}

		@Override
		public ITokenDataWrapper getTokenData() {
			return null;
		}

	}

}
