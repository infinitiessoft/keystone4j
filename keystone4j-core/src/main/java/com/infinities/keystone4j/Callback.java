package com.infinities.keystone4j;

import javax.ws.rs.container.ContainerRequestContext;

public interface Callback {

	void execute(ContainerRequestContext request, ProtectedAction<?> command) throws Exception;
}
