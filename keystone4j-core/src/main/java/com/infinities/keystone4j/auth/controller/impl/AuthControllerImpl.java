package com.infinities.keystone4j.auth.controller.impl;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.action.AuthenticationForTokenAction;
import com.infinities.keystone4j.auth.action.CheckTokenAction;
import com.infinities.keystone4j.auth.action.GetRevocationListAction;
import com.infinities.keystone4j.auth.action.RevokeTokenAction;
import com.infinities.keystone4j.auth.action.ValidateTokenAction;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.auth.model.AuthV3;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.model.SignedWrapper;

public class AuthControllerImpl implements AuthController {

	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final TrustApi trustApi;
	private final PolicyApi policyApi;


	public AuthControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TokenApi tokenApi, TrustApi trustApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.tokenApi = tokenApi;
		this.trustApi = trustApi;
		this.policyApi = policyApi;
	}

	@Override
	public TokenMetadata authenticateForToken(AuthV3 auth) {
		Action<TokenMetadata> command = new AuthenticationForTokenAction(assignmentApi, identityApi, tokenProviderApi,
				tokenApi, trustApi, auth);
		TokenMetadata ret = command.execute();
		return ret;
	}

	@Override
	public void checkToken() {
		Action<TokenMetadata> command = new PolicyCheckDecorator<TokenMetadata>(new CheckTokenAction(assignmentApi,
				identityApi, tokenProviderApi, tokenApi), null, tokenApi, policyApi);
		command.execute();
	}

	@Override
	public void revokeToken() {
		Action<TokenMetadata> command = new PolicyCheckDecorator<TokenMetadata>(new RevokeTokenAction(assignmentApi,
				identityApi, tokenProviderApi, tokenApi), null, tokenApi, policyApi);
		command.execute();
	}

	@Override
	public TokenMetadata validateToken() {
		Action<TokenMetadata> command = new PolicyCheckDecorator<TokenMetadata>(new ValidateTokenAction(assignmentApi,
				identityApi, tokenProviderApi, tokenApi), null, tokenApi, policyApi);
		return command.execute();
	}

	@Override
	public SignedWrapper getRevocationList() {
		Action<SignedWrapper> command = new PolicyCheckDecorator<SignedWrapper>(new GetRevocationListAction(assignmentApi,
				identityApi, tokenProviderApi, tokenApi), null, tokenApi, policyApi);
		return command.execute();
	}

}
