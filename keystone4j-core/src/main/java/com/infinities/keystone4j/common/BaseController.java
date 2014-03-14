package com.infinities.keystone4j.common;

import javax.ws.rs.container.ContainerRequestContext;

public abstract class BaseController {

	private ContainerRequestContext request;


	public void setRequest(ContainerRequestContext request) {
		this.request = request;
	}

	public ContainerRequestContext getRequest() {
		return request;
	}

}
