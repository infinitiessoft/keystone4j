package com.infinities.keystone4j.policy.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeletePolicyAction extends AbstractPolicyAction implements ProtectedAction<Policy> {

	private final String policyid;


	public DeletePolicyAction(PolicyApi policyApi, TokenProviderApi tokenProviderApi, String policyid) {
		super(policyApi, tokenProviderApi);
		this.policyid = policyid;
	}

	@Override
	public MemberWrapper<Policy> execute(ContainerRequestContext request) {
		this.getPolicyApi().deletePolicy(policyid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_polic";
	}

}
