package com.infinities.keystone4j.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.model.CustomResponseStatus;

//keystone.middleware.core.RequestBodySizeLimiter 20141126
@Priority(1004)
public class RequestBodySizeLimiter implements Middleware {

	private final static Logger logger = LoggerFactory.getLogger(RequestBodySizeLimiter.class);
	private final int size;


	public RequestBodySizeLimiter() {
		String maxSize = Config.Instance.getOpt(Config.Type.DEFAULT, "max_request_body_size").asText();
		size = Integer.parseInt(maxSize);
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.debug("enter RequestBodySizeLimiter filter");
		if (requestContext.getLength() > size) {
			requestContext.abortWith(Response.status(CustomResponseStatus.REQUEST_TOO_LARGE).build());
		}

		// keystone.common.utils.LimitingReader
		if (requestContext.getEntityStream().available() > size) {
			requestContext.abortWith(Response.status(CustomResponseStatus.REQUEST_TOO_LARGE).build());
		}
		logger.debug("leave RequestBodySizeLimiter filter");

	}
}
