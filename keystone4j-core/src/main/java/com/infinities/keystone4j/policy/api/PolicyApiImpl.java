package com.infinities.keystone4j.policy.api;

import java.util.List;
import java.util.Map;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.api.command.decorator.ResponseTruncatedCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.notification.Notifications;
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
	private final static String _POLICY = "policy";


	public PolicyApiImpl(PolicyDriver policyDriver) {
		super();
		this.policyDriver = policyDriver;
	}

	@Override
	public Policy createPolicy(String policyid, Policy policy) throws Exception {
		NonTruncatedCommand<Policy> command = Notifications.created(new CreatePolicyCommand(policyDriver, policyid, policy),
				_POLICY, false, 1, null);
		return command.execute();
	}

	@Override
	public List<Policy> listPolicies(Hints hints) throws Exception {
		TruncatedCommand<Policy> command = new ResponseTruncatedCommand<Policy>(new ListPoliciesCommand(policyDriver),
				policyDriver);
		return command.execute(hints);
	}

	@Override
	public Policy getPolicy(String policyid) {
		GetPolicyCommand command = new GetPolicyCommand(policyDriver, policyid);
		return command.execute();
	}

	@Override
	public Policy updatePolicy(String policyid, Policy policy) throws Exception {
		NonTruncatedCommand<Policy> command = Notifications.updated(new UpdatePolicyCommand(policyDriver, policyid, policy),
				_POLICY, false, 1, null);
		return command.execute();
	}

	@Override
	public Policy deletePolicy(String policyid) throws Exception {
		NonTruncatedCommand<Policy> command = Notifications.deleted(new DeletePolicyCommand(policyDriver, policyid),
				_POLICY, false, 1, null);
		return command.execute();
	}

	@Override
	public void enforce(Context context, String action, Map<String, Object> target) throws Exception {
		EnforceCommand command = new EnforceCommand(policyDriver, context, action, target);
		command.execute();
	}

}
