package com.infinities.keystone4j.policy.controller.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.action.CreatePolicyAction;
import com.infinities.keystone4j.policy.action.DeletePolicyAction;
import com.infinities.keystone4j.policy.action.GetPolicyAction;
import com.infinities.keystone4j.policy.action.ListPoliciesAction;
import com.infinities.keystone4j.policy.action.UpdatePolicyAction;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;
import com.infinities.keystone4j.policy.model.PoliciesWrapper;
import com.infinities.keystone4j.policy.model.Policy;
import com.infinities.keystone4j.policy.model.PolicyWrapper;
import com.infinities.keystone4j.token.TokenApi;

public class PolicyV3ControllerImpl implements PolicyV3Controller {

	private final PolicyApi policyApi;
	private final TokenApi tokenApi;
	private final Map<String, Object> parMap;


	public PolicyV3ControllerImpl(PolicyApi policyApi, TokenApi tokenApi) {
		this.policyApi = policyApi;
		this.tokenApi = tokenApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public PolicyWrapper createPolicy(Policy policy) {
		parMap.put("policy", policy);
		Action<Policy> command = new PolicyCheckDecorator<Policy>(new CreatePolicyAction(policyApi, policy), null, tokenApi,
				policyApi, parMap);
		Policy ret = command.execute();
		return new PolicyWrapper(ret);
	}

	@Override
	public PoliciesWrapper listPolicies(String type, int page, int perPage) {
		Action<List<Policy>> command = new FilterCheckDecorator<List<Policy>>(new PaginateDecorator<Policy>(
				new ListPoliciesAction(policyApi, type), page, perPage));

		List<Policy> ret = command.execute();
		return new PoliciesWrapper(ret);
	}

	@Override
	public PolicyWrapper getPolicy(String policyid) {
		parMap.put("policyid", policyid);
		Action<Policy> command = new PolicyCheckDecorator<Policy>(new GetPolicyAction(policyApi, policyid), null, tokenApi,
				policyApi, parMap);
		Policy ret = command.execute();
		return new PolicyWrapper(ret);
	}

	@Override
	public PolicyWrapper updatePolicy(String policyid, Policy policy) {
		parMap.put("policyid", policyid);
		parMap.put("policy", policy);
		Action<Policy> command = new PolicyCheckDecorator<Policy>(new UpdatePolicyAction(policyApi, policyid, policy), null,
				tokenApi, policyApi, parMap);
		Policy ret = command.execute();
		return new PolicyWrapper(ret);
	}

	@Override
	public void deletePolicy(String policyid) {
		parMap.put("policyid", policyid);
		Action<Policy> command = new PolicyCheckDecorator<Policy>(new DeletePolicyAction(policyApi, policyid), null,
				tokenApi, policyApi, parMap);
		command.execute();
	}

}
