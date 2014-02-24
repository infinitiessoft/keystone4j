package com.infinities.keystone4j.policy.command;

import com.google.common.base.Strings;
import com.infinities.keystone4j.exception.NotFoundException;
import com.infinities.keystone4j.exception.PolicyNotFoundException;
import com.infinities.keystone4j.exception.ValidationException;
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
			throw new ValidationException(CANNOT_CHANGE_POLICY_ID);
		}
		try {
			return this.getPolicyDriver().updatePolicy(policyid, policy);
		} catch (NotFoundException e) {
			throw new PolicyNotFoundException(null, policyid);
		}
	}

}
