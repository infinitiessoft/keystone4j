package com.infinities.keystone4j.policy.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.PolicyDriver;

public class PolicyApiFactory implements Factory<PolicyApi> {

	private final PolicyDriver policyDriver;


	@Inject
	public PolicyApiFactory(PolicyDriver policyDriver) {
		this.policyDriver = policyDriver;
	}

	@Override
	public void dispose(PolicyApi arg0) {

	}

	@Override
	public PolicyApi provide() {
		PolicyApi identityApi = new PolicyApiImpl(policyDriver);
		return identityApi;
	}

}
