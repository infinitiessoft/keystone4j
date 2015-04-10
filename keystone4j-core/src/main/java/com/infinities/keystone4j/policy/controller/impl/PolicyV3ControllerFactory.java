package com.infinities.keystone4j.policy.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.policy.controllers.PolicyV3 20141211

public class PolicyV3ControllerFactory extends BaseControllerFactory implements Factory<PolicyV3Controller> {

	private final PolicyApi policyApi;
	private final TokenProviderApi tokenProviderApi;


	@Inject
	public PolicyV3ControllerFactory(PolicyApi policyApi, TokenProviderApi tokenProviderApi) {
		this.policyApi = policyApi;
		this.tokenProviderApi = tokenProviderApi;
	}

	@Override
	public void dispose(PolicyV3Controller arg0) {

	}

	@Override
	public PolicyV3Controller provide() {
		PolicyV3ControllerImpl controller = new PolicyV3ControllerImpl(policyApi, tokenProviderApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
