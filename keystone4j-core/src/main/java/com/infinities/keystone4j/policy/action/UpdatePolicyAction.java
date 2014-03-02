package com.infinities.keystone4j.policy.action;

import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.Policy;

public class UpdatePolicyAction extends AbstractPolicyAction<Policy> {

	private String policyid;
	private Policy policy;


	public UpdatePolicyAction(PolicyApi policyApi, String policyid, Policy policy) {
		super(policyApi);
	}

	@Override
	public Policy execute() {
		return this.getPolicyApi().updatePolicy(policyid, policy);
	}

	@Override
	public String getName() {
		return "update_policy";
	}
}
