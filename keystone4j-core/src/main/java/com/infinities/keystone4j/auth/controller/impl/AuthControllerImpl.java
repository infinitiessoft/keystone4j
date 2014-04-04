package com.infinities.keystone4j.auth.controller.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.action.AuthenticationForTokenAction;
import com.infinities.keystone4j.auth.action.CheckTokenAction;
import com.infinities.keystone4j.auth.action.GetRevocationListAction;
import com.infinities.keystone4j.auth.action.RevokeTokenAction;
import com.infinities.keystone4j.auth.action.ValidateTokenAction;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.auth.AuthV3;
import com.infinities.keystone4j.model.auth.TokenMetadata;
import com.infinities.keystone4j.model.trust.SignedWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class AuthControllerImpl extends BaseController implements AuthController {

	private final static Logger logger = LoggerFactory.getLogger(AuthControllerImpl.class);
	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final TrustApi trustApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public AuthControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TokenApi tokenApi, TrustApi trustApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.tokenApi = tokenApi;
		this.trustApi = trustApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public TokenMetadata authenticateForToken(AuthV3 auth) {
		Action<TokenMetadata> command = new AuthenticationForTokenAction(assignmentApi, identityApi, tokenProviderApi,
				tokenApi, trustApi, auth);
		TokenMetadata ret = command.execute(getRequest());
		logger.debug("generate token id: {}", ret.getTokenid());
		return ret;
	}

	@Override
	public void checkToken() {
		Action<TokenMetadata> command = new PolicyCheckDecorator<TokenMetadata>(new CheckTokenAction(assignmentApi,
				identityApi, tokenProviderApi, tokenApi), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public void revokeToken() {
		Action<TokenMetadata> command = new PolicyCheckDecorator<TokenMetadata>(new RevokeTokenAction(assignmentApi,
				identityApi, tokenProviderApi, tokenApi), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public TokenMetadata validateToken() {
		Action<TokenMetadata> command = new PolicyCheckDecorator<TokenMetadata>(new ValidateTokenAction(assignmentApi,
				identityApi, tokenProviderApi, tokenApi), null, tokenApi, policyApi, parMap);
		return command.execute(getRequest());
	}

	@Override
	public SignedWrapper getRevocationList() {
		Action<SignedWrapper> command = new PolicyCheckDecorator<SignedWrapper>(new GetRevocationListAction(assignmentApi,
				identityApi, tokenProviderApi, tokenApi), null, tokenApi, policyApi, parMap);
		return command.execute(getRequest());
	}
}
