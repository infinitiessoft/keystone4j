package com.infinities.keystone4j.policy.action;

import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.Policy;

public class CreatePolicyAction extends AbstractPolicyAction<Policy> {

	private Policy policy;


	public CreatePolicyAction(PolicyApi policyApi, Policy policy) {
		super(policyApi);
		this.policy = policy;
	}

	@Override
	public Policy execute() {
		Policy ret = policyApi.createPolicy(policy);
		return ret;
	}
}
