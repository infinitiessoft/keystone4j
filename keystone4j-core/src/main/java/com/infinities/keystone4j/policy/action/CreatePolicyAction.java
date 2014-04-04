package com.infinities.keystone4j.policy.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;

public class CreatePolicyAction extends AbstractPolicyAction<Policy> {

	private final Policy policy;


	public CreatePolicyAction(PolicyApi policyApi, Policy policy) {
		super(policyApi);
		this.policy = policy;
	}

	@Override
	public Policy execute(ContainerRequestContext request) {
		Policy ret = policyApi.createPolicy(policy);
		return ret;
	}

	@Override
	public String getName() {
		return "create_policy";
	}
}
