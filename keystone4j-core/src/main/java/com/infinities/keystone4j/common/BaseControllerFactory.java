package com.infinities.keystone4j.common;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;

public abstract class BaseControllerFactory {

	private ContainerRequestContext request;


	@Context
	public void setRequest(ContainerRequestContext request) {
		this.request = request;
	}

	public ContainerRequestContext getRequest() {
		return request;
	}

}
