/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
package com.infinities.keystone4j.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Config;

@Priority(1003)
public class AdminTokenAuthMiddleware implements Middleware {

	private final String adminToken;
	private final Logger logger = LoggerFactory.getLogger(AdminTokenAuthMiddleware.class);


	public AdminTokenAuthMiddleware() {
		adminToken = Config.Instance.getOpt(Config.Type.DEFAULT, "admin_token").asText();
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		logger.debug("enter AdminTokenAuthMiddleware filter");
		String tokenid = null;
		if (requestContext.getHeaders().containsKey(AUTH_TOKEN_HEADER)) {
			tokenid = requestContext.getHeaders().getFirst(AUTH_TOKEN_HEADER).replace("[null]", "").trim();
		}
		KeystoneContext context = (KeystoneContext) requestContext.getProperty(KeystoneContext.CONTEXT_NAME);

		context.setAdmin(adminToken.equals(tokenid));
		requestContext.setProperty(KeystoneContext.CONTEXT_NAME, context);
		logger.debug("leave AdminTokenAuthMiddleware filter");
	}
}
