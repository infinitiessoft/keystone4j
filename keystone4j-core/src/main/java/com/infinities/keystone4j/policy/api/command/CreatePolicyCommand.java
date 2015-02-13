package com.infinities.keystone4j.policy.api.command;

import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.notification.NotifiableCommand;
import com.infinities.keystone4j.policy.PolicyDriver;

public class CreatePolicyCommand extends AbstractPolicyCommand implements NotifiableCommand<Policy> {

	private final String policyid;
	private final Policy policy;


	public CreatePolicyCommand(PolicyDriver policyDriver, String policyid, Policy policy) {
		super(policyDriver);
		this.policyid = policyid;
		this.policy = policy;
	}

	@Override
	public Policy execute() {
		return this.getPolicyDriver().createPolicy(policyid, policy);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return policyid;
		} else if (index == 2) {
			return policy;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
