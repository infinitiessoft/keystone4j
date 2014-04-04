package com.infinities.keystone4j;

import java.text.MessageFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public abstract class PolicyCredentialChecker extends TokenBindValidator {

	private final static Logger logger = LoggerFactory.getLogger(PolicyCredentialChecker.class);
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	public PolicyCredentialChecker(TokenApi tokenApi, PolicyApi policyApi) {
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	protected void checkProtection(KeystoneContext context, Token token, Action<?> command,
			Map<String, PolicyEntity> target, Map<String, Object> parMap) {
		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else {
			String action = MessageFormat.format("identity:{0}", command.getName());
			Token subjectToken = buildPolicyCheckCredentials(action, context, token);
			policyApi.enforce(subjectToken, action, target, parMap, true);
		}
	}

	protected Token buildPolicyCheckCredentials(String action, KeystoneContext context, Token token) {
		logger.debug("RBAC: AUTHORIZING {}", action);

		// Token token = (Token)
		// request.getProperty(Authorization.AUTH_CONTEXT_ENV);
		if (token != null) {
			logger.debug("RBAC: using auth context from the request environment");
			return token;
		}

		try {
			logger.debug("RBAC:building auth context from the incoming auth token");
			token = tokenApi.getToken(context.getTokenid());
			validateTokenBind(context, token);
			return token;
		} catch (Exception e) {
			logger.warn("RBAC:Invalid token");
			throw Exceptions.UnauthorizedException.getInstance();
		}

	}

}
