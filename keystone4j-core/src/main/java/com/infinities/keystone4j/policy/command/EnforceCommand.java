package com.infinities.keystone4j.policy.command;

import java.util.Map;

import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.policy.PolicyDriver;

public class EnforceCommand extends AbstractPolicyCommand<Policy> {

	private final Token token;
	private final String action;
	private final Map<String, PolicyEntity> target;
	private final boolean doRaise;
	private final Map<String, Object> parMap;


	public EnforceCommand(PolicyDriver policyDriver, Token token, String action, Map<String, PolicyEntity> target,
			Map<String, Object> parMap, boolean doRaise) {
		super(policyDriver);
		this.token = token;
		this.action = action;
		this.target = target;
		this.parMap = parMap;
		this.doRaise = doRaise;
	}

	@Override
	public Policy execute() {
		return this.getPolicyDriver().enforce(token, action, target, parMap, doRaise);
	}
}
