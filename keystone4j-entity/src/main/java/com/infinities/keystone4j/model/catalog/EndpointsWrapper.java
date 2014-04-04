package com.infinities.keystone4j.model.catalog;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;
import com.infinities.keystone4j.model.common.Links;

public class EndpointsWrapper {

	private List<Endpoint> endpoints;
	private Links links = new Links();


	public EndpointsWrapper(List<Endpoint> endpoints, ContainerRequestContext context) {
		String baseUrl = context.getUriInfo().getBaseUri().toASCIIString() + "v3/endpoints/";
		this.endpoints = endpoints;
		for (Endpoint endpoint : endpoints) {
			ReferentialLinkUtils.instance.addSelfReferentialLink(endpoint, baseUrl);
		}
		links.setSelf(context.getUriInfo().getRequestUri().toASCIIString());
	}

	public List<Endpoint> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(List<Endpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

}
