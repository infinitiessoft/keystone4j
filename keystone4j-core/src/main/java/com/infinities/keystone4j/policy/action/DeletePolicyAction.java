package com.infinities.keystone4j.policy.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;

public class DeletePolicyAction extends AbstractPolicyAction<Policy> {

	private final String policyid;


	public DeletePolicyAction(PolicyApi policyApi, String policyid) {
		super(policyApi);
		this.policyid = policyid;
	}

	@Override
	public Policy execute(ContainerRequestContext request) {
		return this.getPolicyApi().deletePolicy(policyid);
	}

	@Override
	public String getName() {
		return "delete_polic";
	}

}
