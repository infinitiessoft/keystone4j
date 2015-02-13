package com.infinities.keystone4j.model.assignment.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;

public class DomainWrapper implements MemberWrapper<Domain> {

	private Domain domain;


	public DomainWrapper() {

	}

	public DomainWrapper(Domain domain) {
		this.domain = domain;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(domain,
		// baseUrl);
	}

	@Override
	public void setRef(Domain ref) {
		this.domain = ref;
	}

	@XmlElement(name = "domain")
	@Override
	public Domain getRef() {
		return domain;
	}
}
