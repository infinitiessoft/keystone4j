package com.infinities.keystone4j.filter;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.Environment;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.TokenNotFoundException;
import com.infinities.keystone4j.exception.UnauthorizedException;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Bind;
import com.infinities.keystone4j.token.model.Token;

public class AuthContextMiddleware implements Middleware {

	private final static Logger logger = LoggerFactory.getLogger(AuthContextMiddleware.class);
	private final TokenApi tokenApi;


	@Inject
	public AuthContextMiddleware(TokenApi tokenApi) {
		this.tokenApi = tokenApi;
	}

	// subject_token_head only use in token-related action
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
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
		if (tokenid.equals(adminToken)) {
			return new Token();
		}

		KeystoneContext context = new KeystoneContext();
		context.setTokenid(tokenid);
		HttpServletRequest request = (HttpServletRequest) requestContext.getRequest();
		Environment environment = new Environment();
		environment.setAuthType(request.getAuthType());
		environment.setRemoteUser(request.getRemoteUser());
		context.setEnvironment(environment);

		try {
			Token token = tokenApi.getToken(tokenid);
			validateTokenBind(context, token);

			return token;

		} catch (TokenNotFoundException e) {
			logger.warn("RBAC: Invalid token");
			throw new UnauthorizedException(null);
		}

	}

	private void validateTokenBind(KeystoneContext context, Token token) {
		String bindMode = Config.Instance.getOpt(Config.Type.token, "enforce_token_bind").asText();

		if ("disabled".equals(bindMode)) {
			return;
		}

		Bind bind = new Bind();
		if (token.getBind() != null) {
			bind = token.getBind();
		}
		boolean permissive = bindMode.equals("permissive") || bindMode.equals("strict");

		String name = null;
		if (permissive || "required".equals(bindMode)) {
			name = null;
		} else {
			name = bindMode;
		}

		if (bind == null) {
			if (permissive) {
				return;
			} else {
				logger.info("No bind information present in token");
				throw new UnauthorizedException();
			}
		}

		if (!Strings.isNullOrEmpty(name) && !name.equals(bind.getName())) {
			logger.info("Named bind mode {} not in bind information", name);
			throw new UnauthorizedException();
		}

		String bindType = bind.getBindType();
		String identifier = bind.getIdentifier();

		if ("kerberos".equals(bindType)) {
			if (!"negotiate".equals(context.getEnvironment().getAuthType().toLowerCase())) {
				logger.info("Kerberos credentials required and not present");
				throw new UnauthorizedException();
			}

			if (!identifier.equals(context.getEnvironment().getRemoteUser())) {
				logger.info("Kerberos credentials do not match those in bind");
				throw new UnauthorizedException();
			}

			logger.info("Kerberos bind authentication successful");

		} else if ("permissive".equals(bindMode)) {
			logger.debug("Ignoreing unknown bind for permissive mode: {}: {}", new Object[] { bindType, identifier });
		} else {
			logger.info("Couldn't verify unknown bind: {}: {}", new Object[] { bindType, identifier });
			throw new UnauthorizedException();
		}
	}
}
