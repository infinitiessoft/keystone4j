package com.infinities.keystone4j;

import javax.ws.rs.container.ContainerRequestContext;

public interface Action<T> {

	T execute(ContainerRequestContext context);

	String getName();

}
