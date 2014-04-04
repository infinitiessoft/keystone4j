package com.infinities.keystone4j.model.catalog;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;

public class ServiceWrapper {

	private Service service;


	public ServiceWrapper() {

	}

	public ServiceWrapper(Service service, ContainerRequestContext context) {
		this(service, context.getUriInfo().getBaseUri().toASCIIString() + "v3/services/");
	}

	public ServiceWrapper(Service service, String baseUrl) {
		super();
		this.service = service;
		ReferentialLinkUtils.instance.addSelfReferentialLink(service, baseUrl);
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

}
