package com.infinities.keystone4j.model.catalog;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;
import com.infinities.keystone4j.model.common.Links;

public class ServicesWrapper {

	private List<Service> services;
	private Links links = new Links();


	public ServicesWrapper(List<Service> services, ContainerRequestContext context) {
		String baseUrl = context.getUriInfo().getBaseUri().toASCIIString() + "v3/services/";
		this.services = services;
		for (Service service : services) {
			ReferentialLinkUtils.instance.addSelfReferentialLink(service, baseUrl);
		}
		links.setSelf(context.getUriInfo().getRequestUri().toASCIIString());
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

}
