package com.infinities.keystone4j.token.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class TokenApiFactory implements Factory<TokenApi> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final TrustApi trustApi;
	private final TokenDriver tokenDriver;


	@Inject
	public TokenApiFactory(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TrustApi trustApi, TokenDriver tokenDriver) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.tokenDriver = tokenDriver;
	}

	@Override
	public void dispose(TokenApi arg0) {

	}

	@Override
	public TokenApi provide() {
		TokenApi tokenApi = new TokenApiImpl(assignmentApi, identityApi, tokenProviderApi, trustApi, tokenDriver);
		return tokenApi;
	}

}
