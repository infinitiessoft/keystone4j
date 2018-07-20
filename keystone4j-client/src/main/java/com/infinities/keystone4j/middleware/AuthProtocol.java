/*******************************************************************************
 * Copyright 2015 InfinitiesSoft Solutions Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.infinities.keystone4j.middleware;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.middleware.exception.InvalidUserTokenException;
import com.infinities.keystone4j.middleware.model.TokenWrapper;

public class AuthProtocol extends AuthProtocolBase {

	private final static Logger logger = LoggerFactory.getLogger(AuthProtocol.class);
	public final static String CONFIG_FOLDER = "config";


	// private boolean delayAuthDecision;
	// private boolean includeServiceCatalog;

	public AuthProtocol() throws MalformedURLException {
		super();
	}

	public void call(ContainerRequestContext env) throws IOException {
		logger.debug("Authenticating user token");

		tokenCache.initialize();
		try {
			removeAuthHeaders(env);
			String userToken = getUserTokenFromHeader(env);
			String authType = env.getSecurityContext().getAuthenticationScheme();
			String remoteUser = env.getSecurityContext().getUserPrincipal().getName();
			TokenWrapper tokenInfo = validateUserToken(userToken, authType, remoteUser, true);
			env.setProperty("keystone.token_info", tokenInfo);
			Map<String, String> userHeaders = buildUserHeaders(tokenInfo);
			addHeaders(env, userHeaders);
			logger.debug("AuthProtocal end");
		} catch (InvalidUserTokenException e) {
			if (delayAuthDecision) {
				logger.info("Invalid user token - deferring reject downstream", e);
				Map<String, String> userHeaders = new HashMap<String, String>();
				userHeaders.put("X-Identity-Status", "Invalid");
				addHeaders(env, userHeaders);
			} else {
				logger.info("invalid token, reject request", e);
				rejectRequest(env);
			}
		} catch (ServiceException e) {
			logger.error("Unable to obtain admin token", e);
			env.abortWith(Response.status(Status.SERVICE_UNAVAILABLE).build());
		}

	}

	private void rejectRequest(ContainerRequestContext env) {
		String headerVal = String.format("Keystone uri=\'%s\'", identityServer.getAuthUri());
		Response response = Response.status(Status.UNAUTHORIZED).build();
		response.getHeaders().add("WWW-Authenticate", headerVal);
		env.abortWith(response);
	}

	private void addHeaders(ContainerRequestContext env, Map<String, String> userHeaders) {
		for (Entry<String, String> entry : userHeaders.entrySet()) {
			String envKey = headerToEnvVar(entry.getKey());
			env.getHeaders().add(envKey, entry.getValue());
		}
	}

	private String getUserTokenFromHeader(ContainerRequestContext env) {
		String token = getHeader(env, "X-Auth-Token", getHeader(env, "X-Storage-Token", null));

		if (!Strings.isNullOrEmpty(token)) {
			return token;
		} else {
			if (!delayAuthDecision) {
				logger.warn("Unable to find authentication token in headers");
				logger.debug("Headers: {}", env.getHeaders());
			}
			throw new InvalidUserTokenException("Unable to find token in headers");
		}
	}

	private String getHeader(ContainerRequestContext env, String key, String defaultVal) {
		String envKey = headerToEnvVar(key);
		String obj = env.getHeaderString(envKey);
		if (obj == null) {
			obj = defaultVal;
		}
		return obj;
	}

	private void removeAuthHeaders(ContainerRequestContext env) {
		String[] authHeaders = new String[] { "X-Identity-Status", "X-Domain-Id", "X-Domain-Name", "X-Project-Id",
				"X-Project-Name", "X-Project-Domain-Id", "X-Project-Domain-Name", "X-User-Id", "X-User-Name",
				"X-User-Domain-Id", "X-User-Domain-Name", "X-Roles", "X-Service-Catalog", "X-User", "X-Tenant-Id",
				"X-Tenant-Name", "X-Tenant", "X-Role" };

		logger.debug("Removing headers from request environment");
		removeHeaders(env, authHeaders);
	}

	private void removeHeaders(ContainerRequestContext env, String[] authHeaders) {
		for (String k : authHeaders) {
			String envKey = headerToEnvVar(k);
			try {
				env.getHeaders().remove(envKey);
			} catch (Exception e) {

			}
		}
	}

	private String headerToEnvVar(String key) {
		return key;
		// we don't use wsgi
		// return String.format("HTTP_%s", key.replace("-", "_").toUpperCase());
	}

}
