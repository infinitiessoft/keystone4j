package com.infinities.keystone4j.mock;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.policy.PolicyApi;

public class MockPolicyApiFactory implements Factory<PolicyApi> {

	private final PolicyApi policyApi;


	public MockPolicyApiFactory(PolicyApi policyApi) {
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(PolicyApi arg0) {

	}

	@Override
	public PolicyApi provide() {
		return policyApi;
	}

}
