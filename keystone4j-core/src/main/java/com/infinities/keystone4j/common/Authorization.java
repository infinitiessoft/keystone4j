package com.infinities.keystone4j.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.trust.model.TrustRole;

public class Authorization {

	public final static String AUTH_CONTEXT_ENV = "KEYSTONE_AUTH_CONTEXT";
	private final static Logger logger = LoggerFactory.getLogger(Authorization.class);


	public static AuthContext tokenToAuthContext(Token token) {
		return v3TokenToAuthContext(token);
	}

	private static AuthContext v3TokenToAuthContext(Token token) {
		AuthContext context = new AuthContext();
		context.setUserid(token.getUser().getId());
		if (token.getTrust().getProject() != null) {
			context.setProjectid(token.getTrust().getProject().getId());
		} else {
			logger.debug("RBAC: Procedding without project");
		}

		if (token.getUser().getDomain() != null) {
			context.setDomainid(token.getUser().getDomain().getId());
		}
		if (!token.getTrust().getTrustRoles().isEmpty()) {
			for (TrustRole trustRole : token.getTrust().getTrustRoles()) {
				context.getRoles().add(trustRole.getRole());
			}
		}

		return context;
	}
}
