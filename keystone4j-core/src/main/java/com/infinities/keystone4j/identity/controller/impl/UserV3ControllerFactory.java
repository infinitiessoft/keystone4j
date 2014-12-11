package com.infinities.keystone4j.identity.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.identity.controllers.UserV3 20141211

public class UserV3ControllerFactory extends BaseControllerFactory implements Factory<UserV3Controller> {

	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	@Inject
	public UserV3ControllerFactory(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(UserV3Controller arg0) {

	}

	@Override
	public UserV3Controller provide() {
		UserV3ControllerImpl controller = new UserV3ControllerImpl(identityApi, tokenProviderApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
