package com.infinities.keystone4j.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

public class RequestBodySizeLimiter implements ContainerRequestFilter {

	private final int size;


	public RequestBodySizeLimiter() {
		String maxSize = Config.Instance.getOpt(Config.Type.DEFAULT, "max_request_body_size").asText();
		size = Integer.parseInt(maxSize);
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if (requestContext.getLength() > size) {
			requestContext.abortWith(Response.status(CustomResponseStatus.REQUEST_TOO_LARGE).build());
		}

		if (requestContext.getEntityStream().available() > size) {
			requestContext.abortWith(Response.status(CustomResponseStatus.REQUEST_TOO_LARGE).build());
		}
	}

}
