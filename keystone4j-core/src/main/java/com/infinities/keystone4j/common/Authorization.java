package com.infinities.keystone4j.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.token.TokenData;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.TrustRole;
import com.infinities.keystone4j.token.model.KeystoneToken;

public class Authorization {

	public final static String AUTH_CONTEXT_ENV = "KEYSTONE_AUTH_CONTEXT";
	private final static Logger logger = LoggerFactory.getLogger(Authorization.class);


	@Deprecated
	public static com.infinities.keystone4j.auth.model.AuthContext tokenToAuthContext(TokenDataWrapper token) {
		return v3TokenToAuthContext(token);
	}

	public static AuthContext tokenToAuthContext(KeystoneToken token) {
		AuthContext authContext = new AuthContext(token, false);

		try {
			authContext.setUserid(token.getUserId());
		} catch (Exception e) {
			logger.warn("RBAC: Invalid user data in token");
			throw Exceptions.UnauthorizedException.getInstance();
		}

		if (token.isProjectScoped()) {
			authContext.setProjectid(token.getProjectId());
		} else if (token.isDomainScoped()) {
			authContext.setDomainid(token.getDomainId());
		} else {
			logger.debug("RBAC: Proceeding without project or domain scope");
		}

		if (token.isTrustScoped()) {
			authContext.setIsDelegatedAuth(true);
			authContext.setTrustid(token.getTrustId());
			authContext.setTrustorid(token.getTrustorUserId());
			authContext.setTrusteeid(token.getTrusteeUserId());
		} else {
			authContext.setTrustid(null);
			authContext.setTrustorid(null);
			authContext.setTrusteeid(null);
		}

		List<String> roles = token.getRoleNames();
		if (roles != null) {
			authContext.setRoles(roles);
		}

		if (token.isOauthScoped()) {
			authContext.setIsDelegatedAuth(true);
		}
		authContext.setConsumerid(token.getOauthConsumerId());
		authContext.setAccessTokenid(token.getOauthAccessTokenId());

		if (token.isFederatedUser()) {
			authContext.setGroupIds(token.getFederatedGroupIds());
		}
		return authContext;
	}

	@Deprecated
	private static com.infinities.keystone4j.auth.model.AuthContext v3TokenToAuthContext(TokenDataWrapper token) {
		TokenData tokenData = token.getToken();
		com.infinities.keystone4j.auth.model.AuthContext context = new com.infinities.keystone4j.auth.model.AuthContext();
		context.setUserid(tokenData.getUser().getId());
		if (tokenData.getProject() != null) {
			context.setProjectid(tokenData.getProject().getId());
		} else {
			logger.debug("RBAC: Procedding without project");
		}

		if (tokenData.getDomain() != null) {
			context.setDomainid(tokenData.getUser().getDomain().getId());
		}
		if (!tokenData.getTrust().getTrustRoles().isEmpty()) {
			for (TrustRole trustRole : tokenData.getTrust().getTrustRoles()) {
				context.getRoles().add(trustRole.getRole());
			}
		}

		return context;
	}


	public static class AuthContext {

		private KeystoneToken token;
		private boolean isDelegatedAuth;
		private String userid;
		private String projectid;
		private String domainid;
		private String trustid;
		private String trustorid;
		private String trusteeid;
		private List<String> roles;
		private String consumerid;
		private String accessTokenid;
		private List<String> groupIds;


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

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getProjectid() {
			return projectid;
		}

		public void setProjectid(String projectid) {
			this.projectid = projectid;
		}

		public String getDomainid() {
			return domainid;
		}

		public void setDomainid(String domainid) {
			this.domainid = domainid;
		}

		public String getTrustid() {
			return trustid;
		}

		public void setTrustid(String trustid) {
			this.trustid = trustid;
		}

		public String getTrustorid() {
			return trustorid;
		}

		public void setTrustorid(String trustorid) {
			this.trustorid = trustorid;
		}

		public String getTrusteeid() {
			return trusteeid;
		}

		public void setTrusteeid(String trusteeid) {
			this.trusteeid = trusteeid;
		}

		public List<String> getRoles() {
			return roles;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

		public String getConsumerid() {
			return consumerid;
		}

		public void setConsumerid(String consumerid) {
			this.consumerid = consumerid;
		}

		public String getAccessTokenid() {
			return accessTokenid;
		}

		public void setAccessTokenid(String accessTokenid) {
			this.accessTokenid = accessTokenid;
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
			return "AuthContext [token=" + token + ", isDelegatedAuth=" + isDelegatedAuth + ", userid=" + userid
					+ ", projectid=" + projectid + ", domainid=" + domainid + ", trustid=" + trustid + ", trustorid="
					+ trustorid + ", trusteeid=" + trusteeid + ", roles=" + roles + ", consumerid=" + consumerid
					+ ", accessTokenid=" + accessTokenid + ", groupIds=" + groupIds + "]";
		}

	}

}
