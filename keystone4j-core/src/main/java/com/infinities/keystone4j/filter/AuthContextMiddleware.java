package com.infinities.keystone4j.filter;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.Environment;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.TokenBindValidator;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;

@Priority(1000)
public class AuthContextMiddleware extends TokenBindValidator implements Middleware {

	private final static Logger logger = LoggerFactory.getLogger(AuthContextMiddleware.class);
	private final TokenApi tokenApi;


	@Inject
	public AuthContextMiddleware(TokenApi tokenApi) {
		this.tokenApi = tokenApi;
	}

	// subject_token_head only use in token-related action
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.debug("enter AuthContextMiddleware filter");
		if (!requestContext.getHeaders().containsKey(AUTH_TOKEN_HEADER)) {
			logger.debug("Auth token not in the request headder. Will not build auth context.");
			return;
		}

		if (requestContext.getProperty(Authorization.AUTH_CONTEXT_ENV) != null) {
			logger.warn("Auth context already exists in the request environment");
		}
		Token context = buildAuthContext(requestContext);
		logger.debug("RBAC: auth_context: {}", context);
		requestContext.setProperty(Authorization.AUTH_CONTEXT_ENV, context);

	}

	private Token buildAuthContext(ContainerRequestContext requestContext) {
		String tokenid = requestContext.getHeaders().getFirst(AUTH_TOKEN_HEADER);
		String adminToken = Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText();
		logger.debug("adminToken: {}", adminToken);
		logger.debug("userToken: {}", tokenid);
		if (tokenid.equals(adminToken)) {
			return new Token();
		}

		KeystoneContext context = new KeystoneContext();
		context.setTokenid(tokenid);
		ContainerRequestContext request = (ContainerRequestContext) requestContext.getRequest();
		Environment environment = new Environment();
		environment.setAuthType(request.getSecurityContext().getAuthenticationScheme());
		Principal principal = request.getSecurityContext().getUserPrincipal();
		if (principal != null) {
			environment.setRemoteUser(principal.getName());
		}
		context.setEnvironment(environment);

		try {
			Token token = tokenApi.getToken(tokenid);
			validateTokenBind(context, token);
			return token;
		} catch (Exception e) {
			logger.warn("RBAC: Invalid token", e);
			throw Exceptions.UnauthorizedException.getInstance(null);
		}

	}

}
