package com.infinities.keystone4j.policy.api.command;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyDriver;

public class GetPolicyCommand extends AbstractPolicyCommand implements NonTruncatedCommand<Policy> {

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
