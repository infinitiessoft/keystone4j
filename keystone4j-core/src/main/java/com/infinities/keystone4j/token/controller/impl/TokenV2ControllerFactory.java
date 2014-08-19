package com.infinities.keystone4j.token.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.controller.TokenController;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class TokenV2ControllerFactory extends BaseControllerFactory implements Factory<TokenController> {

	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final TokenProviderApi tokenProviderApi;
	private final TrustApi trustApi;
	private final PolicyApi policyApi;


	@Inject
	public TokenV2ControllerFactory(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenApi tokenApi, TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi) {
		super();
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(TokenController arg0) {

	}

	@Override
	public TokenController provide() {
		TokenV2ControllerImpl controller = new TokenV2ControllerImpl(assignmentApi, catalogApi, identityApi, tokenApi,
				tokenProviderApi, trustApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}
}
