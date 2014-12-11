package com.infinities.keystone4j.model.assignment;

import com.infinities.keystone4j.model.MemberWrapper;

public class DomainWrapper implements MemberWrapper<Domain> {

	private Domain domain;


	public DomainWrapper() {

	}

	public DomainWrapper(Domain domain) {
		this.domain = domain;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(domain,
		// baseUrl);
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	@Override
	public void setRef(Domain ref) {
		this.domain = ref;
	}
}
