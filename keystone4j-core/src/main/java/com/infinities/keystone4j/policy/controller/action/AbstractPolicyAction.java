package com.infinities.keystone4j.policy.controller.action;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.PoliciesWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.model.policy.PolicyWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractPolicyAction extends AbstractAction<Policy> {

	protected PolicyApi policyApi;


	public AbstractPolicyAction(PolicyApi policyApi, TokenProviderApi tokenProviderApi) {
		super(tokenProviderApi);
		this.policyApi = policyApi;
	}

	public PolicyApi getPolicyApi() {
		return policyApi;
	}

	public void setPolicyApi(PolicyApi policyApi) {
		this.policyApi = policyApi;
	}

	@Override
	protected CollectionWrapper<Policy> getCollectionWrapper() {
		return new PoliciesWrapper();
	}

	@Override
	protected MemberWrapper<Policy> getMemberWrapper() {
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
