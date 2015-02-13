package com.infinities.keystone4j.policy.api.command;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyDriver;

public class ListPoliciesCommand extends AbstractPolicyCommand implements TruncatedCommand<Policy> {

	public ListPoliciesCommand(PolicyDriver policyDriver) {
		super(policyDriver);
	}

	@Override
	public List<Policy> execute(Hints hints) {
		return this.getPolicyDriver().listPolicies();
	}

}
