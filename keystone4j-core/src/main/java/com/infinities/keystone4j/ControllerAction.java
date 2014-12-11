package com.infinities.keystone4j;

import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.common.Authorization.AuthContext;
import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.policy.TargetWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

// keystone.common.controller 20141209
public abstract class ControllerAction {

	private final static Logger logger = LoggerFactory.getLogger(ControllerAction.class);
	// private final TokenApi tokenApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public ControllerAction(TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	protected void checkProtection(KeystoneContext context, ContainerRequestContext request, Action command,
			Map<String, PolicyEntity> targetAttr) {
		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else {
			String action = String.format("identity:%s", command.getName());
			Authorization.AuthContext creds = buildPolicyCheckCredentials(action, context, request);

			TargetWrapper policyDict = new TargetWrapper();
			if (targetAttr != null) {
				policyDict.setTarget(targetAttr);
			}

			policyApi.enforce(creds, action, policyDict, true);
			logger.debug("RBAC: Authorization granted");
		}
	}

	protected Authorization.AuthContext buildPolicyCheckCredentials(String action, KeystoneContext context,
			ContainerRequestContext request) {
		logger.debug("RBAC: AUTHORIZING {}", action);

		// Token token = (Token)
		// AuthContext context =
		// request.getProperty(Authorization.AUTH_CONTEXT_ENV);
		if (request.getHeaders().containsKey(Authorization.AUTH_CONTEXT_ENV)) {
			logger.debug("RBAC: using auth context from the request environment");
			return (Authorization.AuthContext) request.getProperty(Authorization.AUTH_CONTEXT_ENV);
		}

		try {
			logger.debug("RBAC: building auth context from the incoming auth token");
			KeystoneToken tokenRef = new KeystoneToken(context.getTokenid(), tokenProviderApi.validToken(context
					.getTokenid()));
			Wsgi.validateTokenBind(context, tokenRef);
			AuthContext authContext = Authorization.tokenToAuthContext(tokenRef);
			return authContext;
		} catch (Exception e) {
			logger.warn("RBAC:Invalid token");
			throw Exceptions.UnauthorizedException.getInstance();
		}

	}

}
