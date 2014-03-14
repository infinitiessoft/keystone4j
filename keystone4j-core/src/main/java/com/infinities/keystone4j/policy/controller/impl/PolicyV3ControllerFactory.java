package com.infinities.keystone4j.policy.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;
import com.infinities.keystone4j.token.TokenApi;

public class PolicyV3ControllerFactory extends BaseControllerFactory implements Factory<PolicyV3Controller> {

	private final PolicyApi policyApi;
	private final TokenApi tokenApi;


	@Inject
	public PolicyV3ControllerFactory(PolicyApi policyApi, TokenApi tokenApi) {
		this.policyApi = policyApi;
		this.tokenApi = tokenApi;
	}

	@Override
	public void dispose(PolicyV3Controller arg0) {

	}

	@Override
	public PolicyV3Controller provide() {
		return new PolicyV3ControllerImpl(policyApi, tokenApi);
	}

}
