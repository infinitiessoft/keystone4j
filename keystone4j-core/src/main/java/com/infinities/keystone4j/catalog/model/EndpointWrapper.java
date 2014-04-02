package com.infinities.keystone4j.catalog.model;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;

public class EndpointWrapper {

	private Endpoint endpoint;


	public EndpointWrapper() {

	}

	public EndpointWrapper(Endpoint endpoint, ContainerRequestContext context) {
		this(endpoint, context.getUriInfo().getBaseUri().toASCIIString() + "v3/endpoints/");
	}

	public EndpointWrapper(Endpoint endpoint, String baseUrl) {
		super();
		this.endpoint = endpoint;
		ReferentialLinkUtils.instance.addSelfReferentialLink(endpoint, baseUrl);
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

}
