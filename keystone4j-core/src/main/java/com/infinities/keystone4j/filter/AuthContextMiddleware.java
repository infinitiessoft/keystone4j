package com.infinities.keystone4j.filter;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.Environment;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.common.model.CustomResponseStatus;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

// keystone.middleware.core.AuthContextMiddleware 20141127
@Priority(1001)
public class AuthContextMiddleware implements Middleware {

	private final static Logger logger = LoggerFactory.getLogger(AuthContextMiddleware.class);
	// private TokenApi tokenApi;
	private final TokenProviderApi tokenProviderApi;


	@Inject
	public AuthContextMiddleware(TokenProviderApi tokenProviderApi) {
		this.tokenProviderApi = tokenProviderApi;
	}

	// subject_token_head only use in token-related action
	@Override
	public void filter(final ContainerRequestContext requestContext) throws IOException {
		logger.debug("enter AuthContextMiddleware filter");
		String authTokenid = null;
		if (requestContext.getHeaders().containsKey(AUTH_TOKEN_HEADER)) {
			authTokenid = requestContext.getHeaders().getFirst(AUTH_TOKEN_HEADER).replace("[null]", "");
		}
		if (Strings.isNullOrEmpty(authTokenid)) {
			logger.debug("Auth token not in the request headder. Will not build auth context.");
			return;
		}
		logger.debug("X-Auth-Token: {},{}", new Object[] { authTokenid, Strings.isNullOrEmpty(authTokenid) });

		if (requestContext.getProperty(Authorization.AUTH_CONTEXT_ENV) != null) {
			logger.warn("Auth context already exists in the request environment");
			return;
		}

		try {
			Authorization.AuthContext context = buildAuthContext(requestContext);
			logger.debug("RBAC: auth_context: {}", context);
			requestContext.setProperty(Authorization.AUTH_CONTEXT_ENV, context);
		} catch (Exception e) {
			logger.warn("authorize fail", e);
			requestContext.abortWith(Response.status(CustomResponseStatus.UNAUTHORIZED).build());
			logger.warn("abort request");
		}
	}

	private Authorization.AuthContext buildAuthContext(ContainerRequestContext requestContext) {
		String tokenid = null;
		if (requestContext.getHeaders().containsKey(AUTH_TOKEN_HEADER)) {
			tokenid = requestContext.getHeaders().getFirst(AUTH_TOKEN_HEADER).replace("[null]", "").trim();
		}
		String adminToken = Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText();
		logger.debug("adminToken: {}", adminToken);
		logger.debug("userToken: {}", tokenid);
		if (tokenid.equals(adminToken)) {
			return new Authorization.AuthContext();
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
			KeystoneToken tokenRef = new KeystoneToken(tokenid, tokenProviderApi.validateToken(tokenid, null));
			Wsgi.validateTokenBind(context, tokenRef);
			return Authorization.tokenToAuthContext(tokenRef);
		} catch (Exception e) {
			logger.warn("RBAC: Invalid token", e);
			throw Exceptions.UnauthorizedException.getInstance();
		}

	}

	// @Inject
	// public void setTokenApi(TokenApi tokenApi) {
	// this.tokenApi = tokenApi;
	// }

	// @Inject
	// public void setTokenProviderApi(TokenProviderApi tokenProviderApi) {
	// this.tokenProviderApi = tokenProviderApi;
	// }

}
