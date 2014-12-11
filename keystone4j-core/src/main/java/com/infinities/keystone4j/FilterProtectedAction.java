package com.infinities.keystone4j;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.model.CollectionWrapper;

public interface FilterProtectedAction<T> extends Action {

	CollectionWrapper<T> execute(ContainerRequestContext context, String... filters) throws Exception;

}
