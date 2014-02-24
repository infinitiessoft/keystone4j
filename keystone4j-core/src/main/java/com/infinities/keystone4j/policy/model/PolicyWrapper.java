package com.infinities.keystone4j.policy.model;

public class PolicyWrapper {

	private Policy policy;


	public PolicyWrapper() {

	}

	public PolicyWrapper(Policy policy) {
		super();
		this.policy = policy;
	}

	public Policy getPolicy() {
		return policy;
	}

	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

}
