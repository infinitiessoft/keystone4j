package com.infinities.keystone4j.policy.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdatePolicyAction extends AbstractPolicyAction implements ProtectedAction<Policy> {

	private String policyid;
	private Policy policy;


	public UpdatePolicyAction(PolicyApi policyApi, TokenProviderApi tokenProviderApi, String policyid, Policy policy) {
		super(policyApi, tokenProviderApi);
	}

	@Override
	public MemberWrapper<Policy> execute(ContainerRequestContext context) throws Exception {
		Policy ref = this.getPolicyApi().updatePolicy(policyid, policy);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_policy";
	}
}
