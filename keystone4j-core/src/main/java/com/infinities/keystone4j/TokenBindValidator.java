package com.infinities.keystone4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.UnauthorizedException;
import com.infinities.keystone4j.token.model.Bind;
import com.infinities.keystone4j.token.model.Token;

public abstract class TokenBindValidator {

	private final static Logger logger = LoggerFactory.getLogger(TokenBindValidator.class);


	// wsgi.validate_token_bind
	protected void validateTokenBind(KeystoneContext context, Token token) {
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
}
