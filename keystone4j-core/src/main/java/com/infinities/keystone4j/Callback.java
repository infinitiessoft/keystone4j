package com.infinities.keystone4j;

import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

public interface Callback {

	void execute(ContainerRequestContext request, Action<?> command, Map<String, Object> parMap);
}
