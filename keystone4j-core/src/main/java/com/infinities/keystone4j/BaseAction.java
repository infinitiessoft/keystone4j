package com.infinities.keystone4j;

import javax.ws.rs.container.ContainerRequestContext;

public abstract class BaseAction<T> implements Action<T> {

	private ContainerRequestContext request;


	public void setRequest(ContainerRequestContext request) {
		this.request = request;
	}

	public ContainerRequestContext getRequest() {
		return request;
	}

}
