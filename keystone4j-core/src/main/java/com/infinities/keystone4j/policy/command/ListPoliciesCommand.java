package com.infinities.keystone4j.policy.command;

import java.util.List;

import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyDriver;

public class ListPoliciesCommand extends AbstractPolicyCommand<List<Policy>> {

	public ListPoliciesCommand(PolicyDriver policyDriver) {
		super(policyDriver);
	}

	@Override
	public List<Policy> execute() {
		return this.getPolicyDriver().listPolicies();
	}

}
