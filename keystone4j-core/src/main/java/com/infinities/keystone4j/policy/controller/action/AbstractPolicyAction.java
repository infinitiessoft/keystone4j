package com.infinities.keystone4j.policy.controller.action;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.model.policy.wrapper.PoliciesWrapper;
import com.infinities.keystone4j.model.policy.wrapper.PolicyWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractPolicyAction extends AbstractAction<Policy> {

	protected PolicyApi policyApi;


	public AbstractPolicyAction(PolicyApi policyApi, TokenProviderApi tokenProviderApi) {
		super(tokenProviderApi, policyApi);
		this.policyApi = policyApi;
	}

	public PolicyApi getPolicyApi() {
		return policyApi;
	}

	public void setPolicyApi(PolicyApi policyApi) {
		this.policyApi = policyApi;
	}

	@Override
	public CollectionWrapper<Policy> getCollectionWrapper() {
		return new PoliciesWrapper();
	}

	@Override
	public MemberWrapper<Policy> getMemberWrapper() {
		return new PolicyWrapper();
	}

	@Override
	public String getCollectionName() {
		return "policies";
	}

	@Override
	public String getMemberName() {
		return "policy";
	}

}
