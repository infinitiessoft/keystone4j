package com.infinities.keystone4j.policy.action;

import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.Policy;

public class DeletePolicyAction extends AbstractPolicyAction<Policy> {

	private final String policyid;


	public DeletePolicyAction(PolicyApi policyApi, String policyid) {
		super(policyApi);
		this.policyid = policyid;
	}

	@Override
	public Policy execute() {
		return this.getPolicyApi().deletePolicy(policyid);
	}

	@Override
	public String getName() {
		return "delete_polic";
	}

}
