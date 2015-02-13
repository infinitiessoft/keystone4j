package com.infinities.keystone4j.policy.api.command;

import com.google.common.base.Strings;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.notification.NotifiableCommand;
import com.infinities.keystone4j.policy.PolicyDriver;

public class UpdatePolicyCommand extends AbstractPolicyCommand implements NotifiableCommand<Policy> {

	private final static String CANNOT_CHANGE_POLICY_ID = "Cannot change policy ID";
	private final String policyid;
	private final Policy policy;


	public UpdatePolicyCommand(PolicyDriver policyDriver, String policyid, Policy policy) {
		super(policyDriver);
		this.policyid = policyid;
		this.policy = policy;
	}

	@Override
	public Policy execute() {
		if (!Strings.isNullOrEmpty(policy.getId()) && !policyid.equals(policy.getId())) {
			throw Exceptions.ValidationException.getInstance(CANNOT_CHANGE_POLICY_ID);
		}
		try {
			return this.getPolicyDriver().updatePolicy(policyid, policy);
		} catch (Exception e) {
			throw Exceptions.PolicyNotFoundException.getInstance(null, policyid);
		}
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
