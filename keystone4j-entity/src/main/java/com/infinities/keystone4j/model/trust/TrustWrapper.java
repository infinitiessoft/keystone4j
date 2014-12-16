package com.infinities.keystone4j.model.trust;

import com.infinities.keystone4j.model.MemberWrapper;

public class TrustWrapper implements MemberWrapper<Trust> {

	private Trust trust;


	public TrustWrapper() {

	}

	public TrustWrapper(Trust trust) {
		this.trust = trust;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(trust,
		// baseUrl);
	}

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

	@Override
	public void setRef(Trust ref) {
		this.trust = ref;
	}
}
