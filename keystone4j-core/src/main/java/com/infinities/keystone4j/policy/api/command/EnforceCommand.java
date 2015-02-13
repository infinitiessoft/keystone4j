package com.infinities.keystone4j.policy.api.command;

import java.util.Map;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyDriver;

public class EnforceCommand extends AbstractPolicyCommand implements NonTruncatedCommand<Policy> {

	private final Context context;
	private final String action;
	private final Map<String, Object> target;


	public EnforceCommand(PolicyDriver policyDriver, Context context, String action, Map<String, Object> target) {
		super(policyDriver);
		this.context = context;
		this.action = action;
		this.target = target;
	}

	@Override
	public Policy execute() throws Exception {
		return this.getPolicyDriver().enforce(context, action, target);
	}
}
