package com.infinities.keystone4j.policy.api.command;

import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.notification.NotifiableCommand;
import com.infinities.keystone4j.policy.PolicyDriver;

public class DeletePolicyCommand extends AbstractPolicyCommand implements NotifiableCommand<Policy> {

	private final String policyid;


	public DeletePolicyCommand(PolicyDriver policyDriver, String policyid) {
		super(policyDriver);
		this.policyid = policyid;
	}

	@Override
	public Policy execute() {
		try {
			this.getPolicyDriver().deletePolicy(policyid);
			return null;
		} catch (Exception e) {
			throw Exceptions.PolicyNotFoundException.getInstance(null, policyid);
		}
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return policyid;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
