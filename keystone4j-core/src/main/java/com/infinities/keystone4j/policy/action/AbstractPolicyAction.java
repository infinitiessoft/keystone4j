package com.infinities.keystone4j.policy.action;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.policy.PolicyApi;

public abstract class AbstractPolicyAction<T> implements Action<T> {

	protected PolicyApi policyApi;


	public AbstractPolicyAction(PolicyApi policyApi) {
		this.policyApi = policyApi;
	}

	public PolicyApi getPolicyApi() {
		return policyApi;
	}

	public void setPolicyApi(PolicyApi policyApi) {
		this.policyApi = policyApi;
	}

}
