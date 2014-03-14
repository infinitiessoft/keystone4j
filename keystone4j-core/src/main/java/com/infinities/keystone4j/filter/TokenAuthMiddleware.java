package com.infinities.keystone4j.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.KeystoneContext;

@Priority(1001)
public class TokenAuthMiddleware implements Middleware {

	private final static String SUBJECT_TOKEN_HEADER = "X-Subject-Token";
	private final static Logger logger = LoggerFactory.getLogger(TokenAuthMiddleware.class);


	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.debug("enter TokenAuthMiddleware filter");
		String tokenid = requestContext.getHeaders().getFirst(AUTH_TOKEN_HEADER);
		KeystoneContext context = (KeystoneContext) requestContext.getProperty(KeystoneContext.CONTEXT_NAME);
		if (context == null) {
			context = new KeystoneContext();
		}

		context.setTokenid(tokenid);

		if (requestContext.getHeaders().containsKey(SUBJECT_TOKEN_HEADER)) {
			context.setSubjectTokenid(requestContext.getHeaders().getFirst(SUBJECT_TOKEN_HEADER));
		}

		requestContext.setProperty(KeystoneContext.CONTEXT_NAME, context);
		logger.debug("leave TokenAuthMiddleware filter");
	}
}
