package com.infinities.keystone4j.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;

//keystone.middleware.core.TokenAuthMiddleware 20141128
@Priority(1002)
public class TokenAuthMiddleware implements Middleware {

	private final static String SUBJECT_TOKEN_HEADER = "X-Subject-Token";
	private final static Logger logger = LoggerFactory.getLogger(TokenAuthMiddleware.class);


	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.debug("enter TokenAuthMiddleware filter");
		String token = null;
		if (requestContext.getHeaders().containsKey(AUTH_TOKEN_HEADER)) {
			token = requestContext.getHeaders().getFirst(AUTH_TOKEN_HEADER).replace("[null]", "").trim();
		}

		KeystoneContext context = null;
		if (requestContext.getPropertyNames().contains(KeystoneContext.CONTEXT_NAME)) {
			context = (KeystoneContext) requestContext.getProperty(KeystoneContext.CONTEXT_NAME);
		}

		if (context == null) {
			context = new KeystoneContext();
		}

		context.setTokenid(token);
		String subjectTokenid = null;
		if (requestContext.getHeaders().containsKey(SUBJECT_TOKEN_HEADER)) {
			subjectTokenid = requestContext.getHeaders().getFirst(SUBJECT_TOKEN_HEADER).replace("[null]", "").trim();
		}
		if (!Strings.isNullOrEmpty(subjectTokenid)) {
			context.setSubjectTokenid(subjectTokenid);
		}

		requestContext.setProperty(KeystoneContext.CONTEXT_NAME, context);
		logger.debug("leave TokenAuthMiddleware filter ");
	}
}
