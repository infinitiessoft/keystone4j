package com.infinities.keystone4j.policy.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;

public class GetPolicyAction extends AbstractPolicyAction<Policy> {

	private final String policyid;


	public GetPolicyAction(PolicyApi policyApi, String policyid) {
		super(policyApi);
		this.policyid = policyid;
	}

	@Override
	public Policy execute(ContainerRequestContext request) {
		return this.getPolicyApi().getPolicy(policyid);
	}

	@Override
	public String getName() {
		return "get_policy";
	}
}
