package com.infinities.keystone4j.policy.command;

import com.infinities.keystone4j.policy.PolicyDriver;
import com.infinities.keystone4j.policy.model.Policy;
import com.infinities.keystone4j.policy.model.Target;
import com.infinities.keystone4j.token.model.Token;

public class EnforceCommand extends AbstractPolicyCommand<Policy> {

	private final Token token;
	private final String action;
	private final Target target;
	private final boolean doRaise;


	public EnforceCommand(PolicyDriver policyDriver, Token token, String action, Target target, boolean doRaise) {
		super(policyDriver);
		this.token = token;
		this.action = action;
		this.target = target;
		this.doRaise = doRaise;
	}

	@Override
	public Policy execute() {
		return this.getPolicyDriver().enforce(token, action, target, doRaise);
	}
}
