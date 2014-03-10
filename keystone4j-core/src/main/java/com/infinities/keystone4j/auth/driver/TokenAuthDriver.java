package com.infinities.keystone4j.auth.driver;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.TokenBindValidator;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.AuthDriver;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.auth.model.AuthInfo;
import com.infinities.keystone4j.auth.model.Identity;
import com.infinities.keystone4j.exception.ForbiddenException;
import com.infinities.keystone4j.exception.UnauthorizedException;
import com.infinities.keystone4j.exception.ValidationException;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.model.TokenDataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class TokenAuthDriver extends TokenBindValidator implements AuthDriver {

	private final static Logger logger = LoggerFactory.getLogger(TokenAuthDriver.class);
	private final static String METHOD = "token";


	@Override
	public void authenticate(KeystoneContext context, AuthInfo authInfo, AuthContext userContext,
			TokenProviderApi tokenProviderApi, IdentityApi identityApi, AssignmentApi assignmentApi) {
		try {
			Identity authPayload = authInfo.getMethodData(METHOD);
			if (Strings.isNullOrEmpty(authPayload.getToken().getId())) {
				throw new ValidationException(null, "id", METHOD);
			}

			String tokenid = authPayload.getToken().getId();
			TokenDataWrapper tokenRef = tokenProviderApi.validateV3Token(tokenid);

			if (tokenRef.getToken().getTrust() != null) {
				throw new ForbiddenException();
			}

			validateTokenBind(context, tokenRef.getToken().getToken());
			Date expiresAt = tokenRef.getToken().getExpireAt();

			userContext.setExpiresAt(expiresAt);
			userContext.setUserid(tokenRef.getToken().getUser().getId());
			userContext.setExtra(tokenRef.getToken().getExtras());
			userContext.getMethodNames().addAll(tokenRef.getToken().getMethods());

		} catch (Exception e) {
			logger.error("authenticate failed", e);
			throw new UnauthorizedException();
		}
	}

	// // wsgi.validate_token_bind
	// private void validateTokenBind(KeystoneContext context, TokenDataWrapper
	// tokenRef) {
	// String bindMode = Config.Instance.getOpt(Config.Type.token,
	// "enforce_token_bind").asText();
	//
	// if (bindMode.equals("disabled")) {
	// return;
	// }
	//
	// Bind bind = tokenRef.getToken().getBind();
	// boolean permissive = bindMode.equals("permissive") ||
	// bindMode.equals("strict");
	// String name = null;
	// if (permissive || bindMode.equals("required")) {
	// name = null;
	// } else {
	// name = bindMode;
	// }
	//
	// if (bind == null) {
	// if (permissive) {
	// return;
	// } else {
	// logger.info("no bind information {} present in token", name);
	// }
	// }
	//
	// if (!Strings.isNullOrEmpty(name) && !bind.getName().equals(name)) {
	// logger.info("Named bind mode {] not in bind information", name);
	// throw new UnauthorizedException();
	// }
	//
	// if (bind.getBindType().equals("kerberos")) {
	// String authType = context.getEnvironment().getAuthType();
	// if (Strings.isNullOrEmpty(authType)) {
	// authType = "";
	// }
	// authType = authType.toLowerCase();
	//
	// if (!(authType.equals("negotiate"))) {
	// logger.info("Kerberos credentials required and not present");
	// throw new UnauthorizedException();
	// }
	//
	// if
	// (!(context.getEnvironment().getRemoteUser().equals(bind.getIdentifier())))
	// {
	// logger.info("Kerberos credentials do not match those in bind");
	// throw new UnauthorizedException();
	// }
	// logger.info("Kerberos bind authentication successful");
	// } else if (bindMode.equals("permissive")) {
	// logger.debug("Ignoring unknown bind for permissive mode: {}, {}",
	// new Object[] { bind.getBindType(), bind.getIdentifier() });
	// } else {
	// logger.info("Couldn't verify unknown bind: {}: {}", new Object[] {
	// bind.getBindType(), bind.getIdentifier() });
	// throw new UnauthorizedException();
	// }
	//
	// }

	@Override
	public String getMethod() {
		return METHOD;
	}
}
