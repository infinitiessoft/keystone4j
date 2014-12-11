package com.infinities.keystone4j.policy.api.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.policy.PolicyDriver;

public abstract class AbstractPolicyCommand<T> implements Command<T> {

	private final PolicyDriver policyDriver;


	public AbstractPolicyCommand(PolicyDriver policyDriver) {
		super();
		this.policyDriver = policyDriver;
	}

	public PolicyDriver getPolicyDriver() {
		return policyDriver;
	}

}
