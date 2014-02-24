package com.infinities.keystone4j.policy.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;

public class PolicyV3ControllerFactory implements Factory<PolicyV3Controller> {

	private final PolicyApi policyApi;


	@Inject
	public PolicyV3ControllerFactory(PolicyApi policyApi) {
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(PolicyV3Controller arg0) {

	}

	@Override
	public PolicyV3Controller provide() {
		return new PolicyV3ControllerImpl(policyApi);
	}

}
