package com.infinities.keystone4j.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Config;

@Priority(1003)
public class AdminTokenAuthMiddleware implements Middleware {

	private final String adminToken;
	private final Logger logger = LoggerFactory.getLogger(AdminTokenAuthMiddleware.class);


	public AdminTokenAuthMiddleware() {
		adminToken = Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText();
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.debug("enter AdminTokenAuthMiddleware filter");
		String tokenid = requestContext.getHeaders().getFirst(AUTH_TOKEN_HEADER);
		KeystoneContext context = (KeystoneContext) requestContext.getProperty(KeystoneContext.CONTEXT_NAME);

		context.setAdmin(adminToken.equals(tokenid));
		requestContext.setProperty(KeystoneContext.CONTEXT_NAME, context);
	}
}
