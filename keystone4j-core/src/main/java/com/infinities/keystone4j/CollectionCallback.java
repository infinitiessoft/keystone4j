package com.infinities.keystone4j;

import javax.ws.rs.container.ContainerRequestContext;

public interface CollectionCallback {

	void execute(ContainerRequestContext request, FilterProtectedAction<?> command);
}
