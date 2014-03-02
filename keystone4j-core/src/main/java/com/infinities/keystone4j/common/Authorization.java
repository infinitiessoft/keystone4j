package com.infinities.keystone4j.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.token.model.TokenData;
import com.infinities.keystone4j.token.model.TokenDataWrapper;
import com.infinities.keystone4j.trust.model.TrustRole;

public class Authorization {

	public final static String AUTH_CONTEXT_ENV = "KEYSTONE_AUTH_CONTEXT";
	private final static Logger logger = LoggerFactory.getLogger(Authorization.class);


	public static AuthContext tokenToAuthContext(TokenDataWrapper token) {
		return v3TokenToAuthContext(token);
	}

	private static AuthContext v3TokenToAuthContext(TokenDataWrapper token) {
		TokenData tokenData = token.getToken();
		AuthContext context = new AuthContext();
		context.setUserid(tokenData.getUser().getId());
		if (tokenData.getProject() != null) {
			context.setProjectid(tokenData.getProject().getId());
		} else {
			logger.debug("RBAC: Procedding without project");
		}

		if (tokenData.getDomain() != null) {
			context.setDomainid(tokenData.getUser().getDomain().getId());
		}
		if (!tokenData.getTrust().getTrustRoles().isEmpty()) {
			for (TrustRole trustRole : tokenData.getTrust().getTrustRoles()) {
				context.getRoles().add(trustRole.getRole());
			}
		}

		return context;
	}
}
