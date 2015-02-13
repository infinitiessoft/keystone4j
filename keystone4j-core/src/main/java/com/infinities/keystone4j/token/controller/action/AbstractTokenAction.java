package com.infinities.keystone4j.token.controller.action;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.action.AbstractControllerAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public abstract class AbstractTokenAction extends AbstractControllerAction {

	private final static Logger logger = LoggerFactory.getLogger(AbstractTokenAction.class);
	protected AssignmentApi assignmentApi;
	protected CatalogApi catalogApi;
	protected IdentityApi identityApi;
	protected TokenProviderApi tokenProviderApi;
	protected TrustApi trustApi;
	protected PolicyApi policyApi;


	public AbstractTokenAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		super();
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.policyApi = policyApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public CatalogApi getCatalogApi() {
		return catalogApi;
	}

	public void setCatalogApi(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	public TokenProviderApi getTokenProviderApi() {
		return tokenProviderApi;
	}

	public void setTokenProviderApi(TokenProviderApi tokenProviderApi) {
		this.tokenProviderApi = tokenProviderApi;
	}

	public TrustApi getTrustApi() {
		return trustApi;
	}

	public void setTrustApi(TrustApi trustApi) {
		this.trustApi = trustApi;
	}

	public void assertAdmin(KeystoneContext context) throws Exception {
		logger.debug("context is admin? {}", context.isAdmin());
		if (!context.isAdmin()) {
			KeystoneToken userTokenRef = null;
			try {
				userTokenRef = new KeystoneToken(context.getTokenid(), tokenProviderApi.validateToken(context.getTokenid(),
						null));
			} catch (Exception e) {
				logger.debug("get keystone token failed,", e);
				throw Exceptions.UnauthorizedException.getInstance(e);
			}
			Wsgi.validateTokenBind(context, userTokenRef);
			Authorization.AuthContext creds = new Authorization.AuthContext();
			creds.setRoles(userTokenRef.getMetadata().getRoles());
			creds.setTrustId(userTokenRef.getMetadata().getTrustId());

			try {
				creds.setUserId(userTokenRef.getUserId());
			} catch (Exception e) {
				logger.debug("Invalid user", e);
				throw Exceptions.UnauthorizedException.getInstance();
			}

			if (userTokenRef.isProjectScoped()) {
				creds.setTenantId(userTokenRef.getProjectId());
			} else {
				logger.debug("Invalid tenant");
				throw Exceptions.UnauthorizedException.getInstance();
			}
			creds.setRoles(userTokenRef.getRoleNames());
			logger.debug("cred roles: {}", creds.getRoles());
			policyApi.enforce(creds, "admin_required", new HashMap<String, Object>());
		}
	}
}
