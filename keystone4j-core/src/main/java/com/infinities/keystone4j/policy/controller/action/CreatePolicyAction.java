package com.infinities.keystone4j.policy.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreatePolicyAction extends AbstractPolicyAction implements ProtectedAction<Policy> {

	private final Policy policy;


	public CreatePolicyAction(PolicyApi policyApi, TokenProviderApi tokenProviderApi, Policy policy) {
		super(policyApi, tokenProviderApi);
		this.policy = policy;
	}

	@Override
	public MemberWrapper<Policy> execute(ContainerRequestContext context) {
		assignUniqueId(policy);
		Policy ref = policyApi.createPolicy(policy);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "create_policy";
	}
}
