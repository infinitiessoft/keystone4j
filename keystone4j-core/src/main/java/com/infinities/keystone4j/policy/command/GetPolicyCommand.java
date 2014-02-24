package com.infinities.keystone4j.policy.command;

import com.infinities.keystone4j.exception.NotFoundException;
import com.infinities.keystone4j.exception.PolicyNotFoundException;
import com.infinities.keystone4j.policy.PolicyDriver;
import com.infinities.keystone4j.policy.model.Policy;

public class GetPolicyCommand extends AbstractPolicyCommand<Policy> {

	private final String policyid;


	public GetPolicyCommand(PolicyDriver policyDriver, String policyid) {
		super(policyDriver);
		this.policyid = policyid;
	}

	@Override
	public Policy execute() {
		try {
			return this.getPolicyDriver().getPolicy(policyid);
		} catch (NotFoundException e) {
			throw new PolicyNotFoundException(null, policyid);
		}
	}

}
