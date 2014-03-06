package com.infinities.keystone4j.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;

public class TokenAuthMiddleware implements Middleware {

	private final static String SUBJECT_TOKEN_HEADER = "X-Subject-Token";


	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
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
	}
}
