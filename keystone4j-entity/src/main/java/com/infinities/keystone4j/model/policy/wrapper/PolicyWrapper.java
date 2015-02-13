package com.infinities.keystone4j.model.policy.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;

public class PolicyWrapper implements MemberWrapper<Policy> {

	private Policy policy;


	public PolicyWrapper() {

	}

	public PolicyWrapper(Policy policy) {
		this.policy = policy;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(policy,
		// baseUrl);
	}

	@Override
	public void setRef(Policy ref) {
		this.policy = ref;
	}

	@XmlElement(name = "policy")
	@Override
	public Policy getRef() {
		return policy;
	}
}
