package com.infinities.keystone4j.decorator;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.TokenNotFoundException;
import com.infinities.keystone4j.exception.UnauthorizedException;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.model.PolicyEntity;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Bind;
import com.infinities.keystone4j.token.model.Token;

//take care of callback of grant 
public class PolicyCheckDecorator<T> extends AbstractActionDecorator<T> {

	private HttpServletRequest request;
	private final Callback callback;
	private final static Logger logger = LoggerFactory.getLogger(PolicyCheckDecorator.class);
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public PolicyCheckDecorator(Action<T> command, Callback callback, TokenApi tokenApi, PolicyApi policyApi,
			Map<String, Object> parMap) {
		super(command);
		this.callback = callback;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		this.parMap = parMap;
	}

	@Override
	public T execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);

		if (context.isAdmin()) {
			logger.warn("RBAC: Bypassing authorization");
		} else if (callback != null) {
			callback.execute(context);
		} else {
			String action = MessageFormat.format("identity:{0}", command.getName());

			Token token = buildPolicyCheckCredentials(action, context);
			Map<String, PolicyEntity> target = Maps.newHashMap();
			if (!Strings.isNullOrEmpty(context.getSubjectTokenid())) {
				Token subjectToken = tokenApi.getToken(context.getSubjectTokenid());
				target.put("user", subjectToken.getUser());
				target.put("domain", subjectToken.getDomain());
				target.put("project", subjectToken.getProject());
			}
			policyApi.enforce(token, action, target, parMap, true);

		}

		return command.execute();
	}

	private Token buildPolicyCheckCredentials(String action, KeystoneContext context) {
		logger.debug("RBAC: AUTHORIZING {}", action);

		Token token = (Token) request.getAttribute(Authorization.AUTH_CONTEXT_ENV);
		if (token != null) {
			logger.debug("RBAC: using auth context from the request environment");
			return token;
		}

		try {
			logger.debug("RBAC:building auth context from the incoming auth token");
			token = tokenApi.getToken(context.getTokenid());
			validateTokenBind(context, token);
			return token;
		} catch (TokenNotFoundException e) {
			logger.warn("RBAC:Invalid token");
			throw new UnauthorizedException();
		}

	}

	// wsgi.validate_token_bind
	private void validateTokenBind(KeystoneContext context, Token token) {
		String bindMode = Config.Instance.getOpt(Config.Type.token, "enforce_token_bind").asText();

		if (bindMode.equals("disabled")) {
			return;
		}

		Bind bind = token.getBind();
		boolean permissive = bindMode.equals("permissive") || bindMode.equals("strict");
		String name = null;
		if (permissive || bindMode.equals("required")) {
			name = null;
		} else {
			name = bindMode;
		}

		if (bind == null) {
			if (permissive) {
				return;
			} else {
				logger.info("no bind information {} present in token", name);
			}
		}

		if (!Strings.isNullOrEmpty(name) && !bind.getName().equals(name)) {
			logger.info("Named bind mode {] not in bind information", name);
			throw new UnauthorizedException();
		}

		if (bind.getBindType().equals("kerberos")) {
			String authType = context.getEnvironment().getAuthType();
			if (Strings.isNullOrEmpty(authType)) {
				authType = "";
			}
			authType = authType.toLowerCase();

			if (!(authType.equals("negotiate"))) {
				logger.info("Kerberos credentials required and not present");
				throw new UnauthorizedException();
			}

			if (!(context.getEnvironment().getRemoteUser().equals(bind.getIdentifier()))) {
				logger.info("Kerberos credentials do not match those in bind");
				throw new UnauthorizedException();
			}
			logger.info("Kerberos bind authentication successful");
		} else if (bindMode.equals("permissive")) {
			logger.debug("Ignoring unknown bind for permissive mode: {}, {}",
					new Object[] { bind.getBindType(), bind.getIdentifier() });
		} else {
			logger.info("Couldn't verify unknown bind: {}: {}", new Object[] { bind.getBindType(), bind.getIdentifier() });
			throw new UnauthorizedException();
		}

	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public String getName() {
		return "policy_check";
	}

}
