package com.infinities.keystone4j.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Config;

public class AdminTokenAuthMiddleware implements Middleware {

	private final String adminToken;


	public AdminTokenAuthMiddleware() {
		adminToken = Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText();
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String tokenid = requestContext.getHeaders().getFirst(AUTH_TOKEN_HEADER);
		KeystoneContext context = (KeystoneContext) requestContext.getProperty(KeystoneContext.CONTEXT_NAME);
		context.setAdmin(tokenid.equals(adminToken));
		requestContext.setProperty(KeystoneContext.CONTEXT_NAME, context);
	}
}
