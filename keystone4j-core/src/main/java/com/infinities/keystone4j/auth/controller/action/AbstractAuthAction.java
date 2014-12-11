package com.infinities.keystone4j.auth.controller.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractAuthAction extends AbstractControllerAction {

	private final static Logger logger = LoggerFactory.getLogger(AbstractAuthAction.class);
	protected AssignmentApi assignmentApi;
	protected CatalogApi catalogApi;
	protected IdentityApi identityApi;
	protected TokenProviderApi tokenProviderApi;
	protected TokenApi tokenApi;


	public AbstractAuthAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TokenApi tokenApi) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		super();
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.tokenApi = tokenApi;
	}

	public TokenProviderApi getTokenProviderApi() {
		return tokenProviderApi;
	}

	public void setTokenProviderApi(TokenProviderApi tokenProviderApi) {
		this.tokenProviderApi = tokenProviderApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public TokenApi getTokenApi() {
		return tokenApi;
	}

	public void setTokenApi(TokenApi tokenApi) {
		this.tokenApi = tokenApi;
	}

	public CatalogApi getCatalogApi() {
		return catalogApi;
	}

	public void setCatalogApi(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	public String getCollectionName() {
		return "tokens";
	}

	public String getMemberName() {
		return "token";
	}

	protected String getBaseUrl(ContainerRequestContext context, String path) {
		String endpoint = Wsgi.getBaseUrl(context, "public");
		if (Strings.isNullOrEmpty(path)) {
			path = getCollectionName();
		}
		return String.format("%s/%s/s", endpoint, "v3", StringUtils.removeStart(path, "/"));
	}


	public class AuthContext {

		private String userid;
		private String projectid;
		private String accessTokenid;
		private String domainid;
		private Calendar expiresAt;
		private List<String> methodNames = new ArrayList<String>();

		private String auditid;


		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			if (!Strings.isNullOrEmpty(userid) && !userid.equals(this.userid)) {
				String msg = String.format(
						"Unable to reconcile identity attribute %s as it has conflicting values %s and %s", "user_id",
						userid, this.userid);
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}
			this.userid = userid;
		}

		public String getProjectid() {
			return projectid;
		}

		public void setProjectid(String projectid) {
			if (!Strings.isNullOrEmpty(projectid) && !projectid.equals(this.projectid)) {
				String msg = String.format(
						"Unable to reconcile identity attribute %s as it has conflicting values %s and %s", "project_id",
						projectid, this.projectid);
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}
			this.projectid = projectid;
		}

		public String getAccessTokenid() {
			return accessTokenid;
		}

		public void setAccessTokenid(String accessTokenid) {
			if (!Strings.isNullOrEmpty(accessTokenid) && !accessTokenid.equals(this.accessTokenid)) {
				String msg = String.format(
						"Unable to reconcile identity attribute %s as it has conflicting values %s and %s",
						"access_token_id", accessTokenid, this.accessTokenid);
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}
			this.accessTokenid = accessTokenid;
		}

		public String getDomainid() {
			return domainid;
		}

		public void setDomainid(String domainid) {
			if (!Strings.isNullOrEmpty(domainid) && !domainid.equals(this.domainid)) {
				String msg = String.format(
						"Unable to reconcile identity attribute %s as it has conflicting values %s and %s", "domain_id",
						domainid, this.domainid);
				throw Exceptions.UnauthorizedException.getInstance(msg);
			}
			this.domainid = domainid;
		}

		public Calendar getExpiresAt() {
			return expiresAt;
		}

		public void setExpiresAt(Calendar expiresAt) {
			if (this.expiresAt != null) {
				if (expiresAt == null) {
					expiresAt = this.expiresAt;
				}
				if (this.expiresAt.compareTo(expiresAt) != 0) {
					logger.info("expires_at has conflicting values {} and {}. Will use the earlist value.", new Object[] {
							this.expiresAt, expiresAt });
				}
			}
			if (this.expiresAt.compareTo(expiresAt) >= 0) {
				this.expiresAt = expiresAt;
			}
		}

		public List<String> getMethodNames() {
			return methodNames;
		}

		public void setMethodNames(List<String> methodNames) {
			this.methodNames = methodNames;
		}

		public String getAuditid() {
			return auditid;
		}

		public void setAuditid(String auditid) {
			this.auditid = auditid;
		}

	}

}
