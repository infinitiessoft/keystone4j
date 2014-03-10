package com.infinities.keystone4j;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.exception.TokenNotFoundException;
import com.infinities.keystone4j.exception.UnauthorizedException;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;

public abstract class PolicyCredentialChecker extends TokenBindValidator {

	private final static Logger logger = LoggerFactory.getLogger(PolicyCredentialChecker.class);
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private HttpServletRequest request;


	public PolicyCredentialChecker(TokenApi tokenApi, PolicyApi policyApi) {
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	protected void checkProtection(KeystoneContext context, Action<?> command, Map<String, PolicyEntity> target,
			Map<String, Object> parMap) {
		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else {
			String action = MessageFormat.format("identity:{0}", command.getName());
			Token token = buildPolicyCheckCredentials(action, context);
			policyApi.enforce(token, action, target, parMap, true);
		}
	}

	protected Token buildPolicyCheckCredentials(String action, KeystoneContext context) {
		logger.debug("RBAC: AUTHORIZING {}", action);

		Token token = (Token) request.getAttribute(Authorization.AUTH_CONTEXT_ENV);
		if (token != null) {
			logger.debug("RBAC: using auth context from the request environment");
			return token;
		}

		try {
			logger.debug("RBAC:building auth context from the incoming auth token");
			token = tokenApi.getToken(context.getTokenid());
			validateTokenBind(context, token);
			return token;
		} catch (TokenNotFoundException e) {
			logger.warn("RBAC:Invalid token");
			throw new UnauthorizedException();
		}

	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
