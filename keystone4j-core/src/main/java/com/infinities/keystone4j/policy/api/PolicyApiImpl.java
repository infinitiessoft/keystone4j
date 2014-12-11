package com.infinities.keystone4j.policy.api;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.model.policy.PolicyEntity;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.PolicyDriver;
import com.infinities.keystone4j.policy.api.command.CreatePolicyCommand;
import com.infinities.keystone4j.policy.api.command.DeletePolicyCommand;
import com.infinities.keystone4j.policy.api.command.EnforceCommand;
import com.infinities.keystone4j.policy.api.command.GetPolicyCommand;
import com.infinities.keystone4j.policy.api.command.ListPoliciesCommand;
import com.infinities.keystone4j.policy.api.command.UpdatePolicyCommand;

public class PolicyApiImpl implements PolicyApi {

	private final PolicyDriver policyDriver;


	public PolicyApiImpl(PolicyDriver policyDriver) {
		super();
		this.policyDriver = policyDriver;
	}

	@Override
	public Policy createPolicy(Policy policy) {
		CreatePolicyCommand command = new CreatePolicyCommand(policyDriver, policy);
		return command.execute();
	}

	@Override
	public List<Policy> listPolicies() {
		ListPoliciesCommand command = new ListPoliciesCommand(policyDriver);
		return command.execute();
	}

	@Override
	public Policy getPolicy(String policyid) {
		GetPolicyCommand command = new GetPolicyCommand(policyDriver, policyid);
		return command.execute();
	}

	@Override
	public Policy updatePolicy(String policyid, Policy policy) {
		UpdatePolicyCommand command = new UpdatePolicyCommand(policyDriver, policyid, policy);
		return command.execute();
	}

	@Override
	public Policy deletePolicy(String policyid) {
		DeletePolicyCommand command = new DeletePolicyCommand(policyDriver, policyid);
		return command.execute();
	}

	@Override
	public void enforce(Token token, String action, Map<String, PolicyEntity> target, Map<String, Object> parMap,
			boolean doRaise) {
		EnforceCommand command = new EnforceCommand(policyDriver, token, action, target, parMap, doRaise);
		command.execute();
	}

}
