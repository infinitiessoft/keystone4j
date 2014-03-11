package com.infinities.keystone4j;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;

public class KeystoneUtils extends TokenBindValidator {

	private final Logger logger = LoggerFactory.getLogger(KeystoneUtils.class);
	private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	// private final static String ENFORCE_TOKEN_BIND = "enforce_token_bind";
	// private final static String DISABLED = "disabled";
	// private final static String PERMISSIVE = "permissive";
	// private final static String STRICT = "strict";
	// private final static String REQUIRED = "required";
	private final static String INVALID_TOKEN = "Invalid token";
	private final static String UNAUTHORIZED = "Unauthorized";
	// private final static String NO_BIND_INFORMATION =
	// "No bind information present in token";
	// private final static String BIND_INFORMATION_NOT_PRESENT =
	// "Named bind mode {0} not in bind information";
	// private final static String NEGOTIATE = "negotiate";
	// private final static String KERBEROS = "kerberos";
	// private final static String KERBEROS_NOT_PRESENT =
	// "Kerberos credentials required and not present";
	// private final static String KERBEROS_NOT_MATCH =
	// "Kerberos credentials do not match those in bind";
	// private final static String KERBEROS_BIND_SUCCESSFUL =
	// "Kerberos bind authentication successful";
	// private final static String IGNORING_UNKNOWN_BIND =
	// "Ignoring unknown bind for permissice mode: {0}: {1}";
	// private final static String COULD_NOT_VERIFY_UNKNOWN_BIND =
	// "Couldn't verify unknown bind for permissive bind: {0}:{1}";
	private TokenApi tokenApi;
	private AssignmentApi assignmentApi;
	private PolicyApi policyApi;


	// TODO initiate tokenApi....
	public KeystoneUtils() {

	}

	// used as _normalize_domain_id
	public Domain getDomainForRequest(KeystoneContext context) {
		if (context.isAdmin()) {
			return assignmentApi.getDomain(Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).asText());
		}

		try {
			Token token = tokenApi.getToken(context.getTokenid());

			try {
				return token.getUser().getDomain();
			} catch (Exception e) {
				return assignmentApi.getDomain(Config.Instance.getOpt(Config.Type.identity, DEFAULT_DOMAIN_ID).asText());
			}

		} catch (Exception TokenNotFound) {
			logger.warn(INVALID_TOKEN);
			throw Exceptions.UnauthorizedException.getInstance();
		}

	}

	public void assertAdmin(KeystoneContext context) {
		if (!context.isAdmin()) {
			Token token = null;
			try {
				token = tokenApi.getToken(context.getTokenid());
			} catch (Exception e) {
				logger.error(UNAUTHORIZED, e);
				throw Exceptions.UnauthorizedException.getInstance();
			}

			validateTokenBind(context, token);

			policyApi.enforce(token, "admin_required", null, new HashMap<String, Object>(), true);

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
}
