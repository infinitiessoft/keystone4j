package com.infinities.keystone4j.policy.command;

import com.infinities.keystone4j.policy.PolicyDriver;
import com.infinities.keystone4j.policy.model.Policy;

public class CreatePolicyCommand extends AbstractPolicyCommand<Policy> {

	private final Policy policy;


	public CreatePolicyCommand(PolicyDriver policyDriver, Policy policy) {
		super(policyDriver);
		this.policy = policy;
	}

	@Override
	public Policy execute() {
		return this.getPolicyDriver().createPolicy(policy);
	}

}
