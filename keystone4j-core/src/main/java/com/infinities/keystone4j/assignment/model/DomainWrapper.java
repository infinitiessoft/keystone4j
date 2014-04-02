package com.infinities.keystone4j.assignment.model;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;

public class DomainWrapper {

	private Domain domain;


	public DomainWrapper() {

	}

	public DomainWrapper(Domain domain, ContainerRequestContext context) {
		this(domain, context.getUriInfo().getBaseUri().toASCIIString() + "v3/domains/");
	}

	public DomainWrapper(Domain domain, String baseUrl) {
		this.domain = domain;
		ReferentialLinkUtils.instance.addSelfReferentialLink(domain, baseUrl);
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

}
