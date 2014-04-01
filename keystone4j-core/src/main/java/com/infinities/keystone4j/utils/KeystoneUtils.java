package com.infinities.keystone4j.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.TokenBindValidator;
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
	private final static String INVALID_TOKEN = "Invalid token";
	private final static String UNAUTHORIZED = "Unauthorized";


	// used as _normalize_domain_id
	public Domain getDomainForRequest(AssignmentApi assignmentApi, TokenApi tokenApi, KeystoneContext context) {
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

	public void assertAdmin(PolicyApi policyApi, TokenApi tokenApi, KeystoneContext context) {
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

	public User getUser(TokenApi tokenApi, KeystoneContext context) {
		if (!Strings.isNullOrEmpty(context.getTokenid())) {
			String tokenid = context.getTokenid();
			Token token = tokenApi.getToken(tokenid);
			return token.getUser();
		}
		return null;
	}

	public URL getURL(String filePath) {
		File file = new File(filePath);
		try {
			return file.toURI().toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
}
