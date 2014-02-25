package com.infinities.keystone4j.filter;

import javax.ws.rs.container.ContainerRequestFilter;

public interface Middleware extends ContainerRequestFilter {

	final static String AUTH_TOKEN_HEADER = "X-Auth-Token";
}
