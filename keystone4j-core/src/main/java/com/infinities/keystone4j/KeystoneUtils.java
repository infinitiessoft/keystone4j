package com.infinities.keystone4j;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.TokenNotFoundException;
import com.infinities.keystone4j.exception.UnauthorizedException;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Bind;
import com.infinities.keystone4j.token.model.Token;

public class KeystoneUtils {

	private final Logger logger = LoggerFactory.getLogger(KeystoneUtils.class);
	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final static String ENFORCE_TOKEN_BIND = "enforce_token_bind";
	private final static String DISABLED = "disabled";
	private final static String PERMISSIVE = "permissive";
	private final static String STRICT = "strict";
	private final static String REQUIRED = "required";
	private final static String INVALID_TOKEN = "Invalid token";
	private final static String UNAUTHORIZED = "Unauthorized";
	private final static String NO_BIND_INFORMATION = "No bind information present in token";
	private final static String BIND_INFORMATION_NOT_PRESENT = "Named bind mode {0} not in bind information";
	private final static String NEGOTIATE = "negotiate";
	private final static String KERBEROS = "kerberos";
	private final static String KERBEROS_NOT_PRESENT = "Kerberos credentials required and not present";
	private final static String KERBEROS_NOT_MATCH = "Kerberos credentials do not match those in bind";
	private final static String KERBEROS_BIND_SUCCESSFUL = "Kerberos bind authentication successful";
	private final static String IGNORING_UNKNOWN_BIND = "Ignoring unknown bind for permissice mode: {0}: {1}";
	private final static String COULD_NOT_VERIFY_UNKNOWN_BIND = "Couldn't verify unknown bind for permissive bind: {0}:{1}";
	private TokenApi tokenApi;
	private AssignmentApi assignmentApi;
	private PolicyApi policyApi;


	// TODO initiate tokenApi....
	public KeystoneUtils() {

	}

	// used as _normalize_domain_id
	public Domain getDomainForRequest(KeystoneContext context) {
		if (context.isAdmin()) {
			return assignmentApi.getDomain(Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).getText());
		}

		try {
			Token token = tokenApi.getToken(context.getTokenid());

			try {
				return token.getUser().getDomain();
			} catch (Exception e) {
				return assignmentApi.getDomain(Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).getText());
			}

		} catch (Exception TokenNotFound) {
			logger.warn(INVALID_TOKEN);
			throw new UnauthorizedException();
		}

	}

	public void assertAdmin(KeystoneContext context) {
		if (!context.isAdmin()) {
			Token token = null;
			try {
				token = tokenApi.getToken(context.getTokenid());
			} catch (TokenNotFoundException e) {
				logger.error(UNAUTHORIZED, e);
				throw new UnauthorizedException();
			}

			validateTokenBind(context, token);

			policyApi.enforce(token, "admin_required", null, true);

		}
	}

	public User getUser(KeystoneContext context) {
		if (!Strings.isNullOrEmpty(context.getTokenid())) {
			String tokenid = context.getTokenid();
			Token token = tokenApi.getToken(tokenid);
			return token.getUser();
		}
		return null;
	}

	private void validateTokenBind(KeystoneContext context, Token token) {

		String bindMode = Config.Instance.getOpt(Config.Type.token, ENFORCE_TOKEN_BIND).getText();
		if (DISABLED.equals(bindMode)) {
			return;
		}

		Bind bind = token.getBind();
		boolean permissive = PERMISSIVE.equals(bindMode) || STRICT.equals(bindMode);
		String name = null;
		if (!(permissive || REQUIRED.equals(bindMode))) {
			name = bindMode;
		}

		if (bind != null && !Strings.isNullOrEmpty(bind.getName())) {
			if (permissive) {
				return;
			} else {
				logger.info(NO_BIND_INFORMATION);
				throw new UnauthorizedException();
			}
		}

		if (!Strings.isNullOrEmpty(name) && !name.equals(bind.getName())) {
			String msg = MessageFormat.format(BIND_INFORMATION_NOT_PRESENT, name);
			logger.info(msg);
			throw new UnauthorizedException();
		}

		if (KERBEROS.equals(bind.getBindType())) {
			String authType = context.getEnvironment().getAuthType().toLowerCase();
			if (!NEGOTIATE.equals(authType)) {
				logger.info(KERBEROS_NOT_PRESENT);
				throw new UnauthorizedException();
			}
			String remoteUser = context.getEnvironment().getRemoteUser();
			if (!remoteUser.equals(bind.getIdentifier())) {
				logger.info(KERBEROS_NOT_MATCH);
				throw new UnauthorizedException();
			}
			logger.info(KERBEROS_BIND_SUCCESSFUL);
		} else if (PERMISSIVE.equals(bindMode)) {
			String msg = MessageFormat.format(IGNORING_UNKNOWN_BIND, bind.getBindType(), bind.getIdentifier());
			logger.debug(msg);
		} else {
			String msg = MessageFormat.format(COULD_NOT_VERIFY_UNKNOWN_BIND, bind.getBindType(), bind.getIdentifier());
			logger.info(msg);
			throw new UnauthorizedException();
		}

	}
}
