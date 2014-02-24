package com.infinities.keystone4j.policy.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "policies")
public class PoliciesWrapper {

	private List<Policy> policies;


	public PoliciesWrapper(List<Policy> policies) {
		super();
		this.policies = policies;
	}

	public List<Policy> getPolicies() {
		return policies;
	}

	public void setPolicies(List<Policy> policies) {
		this.policies = policies;
	}

}
