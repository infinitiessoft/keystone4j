package com.infinities.keystone4j.policy.command;

import com.google.common.base.Strings;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.policy.PolicyDriver;
import com.infinities.keystone4j.policy.model.Policy;

public class UpdatePolicyCommand extends AbstractPolicyCommand<Policy> {

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
		if (!Strings.isNullOrEmpty(policy.getId()) && policyid.equals(policy.getId())) {
			throw Exceptions.ValidationException.getInstance(CANNOT_CHANGE_POLICY_ID);
		}
		try {
			return this.getPolicyDriver().updatePolicy(policyid, policy);
		} catch (Exception e) {
			throw Exceptions.PolicyNotFoundException.getInstance(null, policyid);
		}
	}

}
