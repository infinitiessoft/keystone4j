package com.infinities.keystone4j.auth.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class AuthControllerFactory extends BaseControllerFactory implements Factory<AuthController> {

	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final PolicyApi policyApi;
	private final CatalogApi catalogApi;


	@Inject
	public AuthControllerFactory(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(AuthController arg0) {

	}

	@Override
	public AuthController provide() {
		AuthControllerImpl controller = new AuthControllerImpl(assignmentApi, catalogApi, identityApi, tokenProviderApi,
				trustApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
