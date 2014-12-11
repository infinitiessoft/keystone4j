package com.infinities.keystone4j.model.policy;

import com.infinities.keystone4j.model.MemberWrapper;

public class PolicyWrapper implements MemberWrapper<Policy> {

	private Policy policy;


	public PolicyWrapper() {

	}

	public PolicyWrapper(Policy policy) {
		this.policy = policy;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(policy,
		// baseUrl);
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	@Override
	public void setRef(Policy ref) {
		this.policy = ref;
	}
}
