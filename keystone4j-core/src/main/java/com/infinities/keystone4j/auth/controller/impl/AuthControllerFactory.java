package com.infinities.keystone4j.auth.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class AuthControllerFactory implements Factory<AuthController> {

	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final TrustApi trustApi;


	@Inject
	public AuthControllerFactory(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TokenApi tokenApi, TrustApi trustApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.tokenApi = tokenApi;
		this.trustApi = trustApi;
	}

	@Override
	public void dispose(AuthController arg0) {

	}

	@Override
	public AuthController provide() {
		return new AuthControllerImpl(assignmentApi, identityApi, tokenProviderApi, tokenApi, trustApi);
	}

}
