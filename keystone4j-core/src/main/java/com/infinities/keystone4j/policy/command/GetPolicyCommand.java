package com.infinities.keystone4j.policy.command;

import com.infinities.keystone4j.exception.Exceptions;
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
		} catch (Exception e) {
			throw Exceptions.PolicyNotFoundException.getInstance(null, policyid);
		}
	}

}
