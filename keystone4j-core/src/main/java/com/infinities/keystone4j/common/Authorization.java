package com.infinities.keystone4j.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.token.model.KeystoneToken;

public class Authorization {

	public final static String AUTH_CONTEXT_ENV = "KEYSTONE_AUTH_CONTEXT";
	private final static Logger logger = LoggerFactory.getLogger(Authorization.class);


	// @Deprecated
	// public static com.infinities.keystone4j.auth.model.AuthContext
	// tokenToAuthContext(TokenDataWrapper token) {
	// return v3TokenToAuthContext(token);
	// }

	public static AuthContext tokenToAuthContext(KeystoneToken token) {
		AuthContext authContext = new AuthContext(token, false);

		try {
			authContext.setUserId(token.getUserId());
		} catch (Exception e) {
			logger.warn("RBAC: Invalid user data in token");
			throw Exceptions.UnauthorizedException.getInstance();
		}

		if (token.isProjectScoped()) {
			authContext.setProjectId(token.getProjectId());
		} else if (token.isDomainScoped()) {
			authContext.setDomainId(token.getDomainId());
		} else {
			logger.debug("RBAC: Proceeding without project or domain scope");
		}

		if (token.isTrustScoped()) {
			authContext.setIsDelegatedAuth(true);
			authContext.setTrustId(token.getTrustId());
			authContext.setTrustorid(token.getTrustorUserId());
			authContext.setTrusteeId(token.getTrusteeUserId());
		} else {
			authContext.setTrustId(null);
			authContext.setTrustorid(null);
			authContext.setTrusteeId(null);
		}

		List<String> roles = token.getRoleNames();
		if (roles != null) {
			authContext.setRoles(roles);
		}

		if (token.isOauthScoped()) {
			authContext.setIsDelegatedAuth(true);
		}
		authContext.setConsumerId(token.getOauthConsumerId());
		authContext.setAccessTokenId(token.getOauthAccessTokenId());

		if (token.isFederatedUser()) {
			authContext.setGroupIds(token.getFederatedGroupIds());
		}
		return authContext;
	}


	// @Deprecated
	// private static com.infinities.keystone4j.auth.model.AuthContext
	// v3TokenToAuthContext(TokenDataWrapper token) {
	// TokenData tokenData = token.getToken();
	// com.infinities.keystone4j.auth.model.AuthContext context = new
	// com.infinities.keystone4j.auth.model.AuthContext();
	// context.setUserid(tokenData.getUser().getId());
	// if (tokenData.getProject() != null) {
	// context.setProjectid(tokenData.getProject().getId());
	// } else {
	// logger.debug("RBAC: Procedding without project");
	// }
	//
	// if (tokenData.getDomain() != null) {
	// context.setDomainid(tokenData.getUser().getDomain().getId());
	// }
	// if (!tokenData.getTrust().getTrustRoles().isEmpty()) {
	// for (TrustRole trustRole : tokenData.getTrust().getTrustRoles()) {
	// context.getRoles().add(trustRole.getRole());
	// }
	// }
	//
	// return context;
	// }

	public static class AuthContext implements Context {

		private KeystoneToken token;
		private boolean isDelegatedAuth;
		private String userId;
		private String projectId;
		private String domainId;
		private String trustId;
		private String trustorId;
		private String trusteeId;
		private List<String> roles;
		private String consumerId;
		private String accessTokenId;
		private List<String> groupIds;

		private String tenantId;


		public AuthContext() {

		}

		public AuthContext(KeystoneToken token, boolean isDelegatedAuth) {
			this.token = token;
			this.isDelegatedAuth = isDelegatedAuth;
		}

		public KeystoneToken getToken() {
			return token;
		}

		public void setToken(KeystoneToken token) {
			this.token = token;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getProjectId() {
			return projectId;
		}

		public void setProjectId(String projectId) {
			this.projectId = projectId;
		}

		public String getDomainId() {
			return domainId;
		}

		public void setDomainId(String domainId) {
			this.domainId = domainId;
		}

		public String getTrustId() {
			return trustId;
		}

		public void setTrustId(String trustId) {
			this.trustId = trustId;
		}

		public String getTrustorId() {
			return trustorId;
		}

		public void setTrustorid(String trustorId) {
			this.trustorId = trustorId;
		}

		public String getTrusteeId() {
			return trusteeId;
		}

		public void setTrusteeId(String trusteeId) {
			this.trusteeId = trusteeId;
		}

		@Override
		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

		public String getConsumerId() {
			return consumerId;
		}

		public void setConsumerId(String consumerId) {
			this.consumerId = consumerId;
		}

		public String getAccessTokenId() {
			return accessTokenId;
		}

		public void setAccessTokenId(String accessTokenId) {
			this.accessTokenId = accessTokenId;
		}

		public List<String> getGroupIds() {
			return groupIds;
		}

		public void setGroupIds(List<String> groupIds) {
			this.groupIds = groupIds;
		}

		public boolean isDelegatedAuth() {
			return isDelegatedAuth;
		}

		public void setIsDelegatedAuth(boolean isDelegatedAuth) {
			this.isDelegatedAuth = isDelegatedAuth;
		}

		@Override
		public String toString() {
			return "AuthContext [token=" + token + ", isDelegatedAuth=" + isDelegatedAuth + ", userid=" + userId
					+ ", projectid=" + projectId + ", domainid=" + domainId + ", trustid=" + trustId + ", trustorid="
					+ trustorId + ", trusteeid=" + trusteeId + ", roles=" + roles + ", consumerid=" + consumerId
					+ ", accessTokenid=" + accessTokenId + ", groupIds=" + groupIds + "]";
		}

		public String getTenantId() {
			return tenantId;
		}

		public void setTenantId(String tenantId) {
			this.tenantId = tenantId;
		}

	}

}
