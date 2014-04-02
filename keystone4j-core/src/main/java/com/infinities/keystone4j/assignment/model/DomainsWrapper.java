package com.infinities.keystone4j.assignment.model;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;
import com.infinities.keystone4j.common.model.Links;

public class DomainsWrapper {

	private List<Domain> domains;

	private Links links = new Links();


	public DomainsWrapper(List<Domain> domains, ContainerRequestContext context) {
		String baseUrl = context.getUriInfo().getBaseUri().toASCIIString() + "v3/domains/";
		this.domains = domains;
		for (Domain domain : domains) {
			ReferentialLinkUtils.instance.addSelfReferentialLink(domain, baseUrl);
		}
		links.setSelf(context.getUriInfo().getRequestUri().toASCIIString());
	}

	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

}
