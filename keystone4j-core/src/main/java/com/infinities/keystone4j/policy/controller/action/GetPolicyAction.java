package com.infinities.keystone4j.policy.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetPolicyAction extends AbstractPolicyAction implements ProtectedAction<Policy> {

	private final String policyid;


	public GetPolicyAction(PolicyApi policyApi, TokenProviderApi tokenProviderApi, String policyid) {
		super(policyApi, tokenProviderApi);
		this.policyid = policyid;
	}

	@Override
	public MemberWrapper<Policy> execute(ContainerRequestContext context) {
		Policy ref = this.getPolicyApi().getPolicy(policyid);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "get_policy";
	}
}
