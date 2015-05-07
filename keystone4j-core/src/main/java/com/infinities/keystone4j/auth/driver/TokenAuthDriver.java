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
package com.infinities.keystone4j.auth.driver;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.AuthDriver;
import com.infinities.keystone4j.auth.model.AuthInfo;
import com.infinities.keystone4j.common.Wsgi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.auth.AuthData;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class TokenAuthDriver implements AuthDriver {

	private final static Logger logger = LoggerFactory.getLogger(TokenAuthDriver.class);
	private final static String METHOD = "token";


	@Override
	public void authenticate(KeystoneContext context, AuthData authPayload,
			com.infinities.keystone4j.auth.controller.action.AbstractAuthAction.AuthContext userContext,
			TokenProviderApi tokenProviderApi, IdentityApi identityApi, AssignmentApi assignmentApi) {
		try {
			if (Strings.isNullOrEmpty(authPayload.getId())) {
				throw Exceptions.ValidationException.getInstance(null, "id", METHOD);
			}

			String tokenid = authPayload.getId();
			TokenDataWrapper response = tokenProviderApi.validateV3Token(tokenid);
			KeystoneToken tokenRef = new KeystoneToken(tokenid, response);

			// TODO ignore oauth_scoped
			if (tokenRef.isOauthScoped() || tokenRef.isTrustScoped()) {
				throw Exceptions.ForbiddenException.getInstance();
			}

			Wsgi.validateTokenBind(context, tokenRef);

			String tokenAuditId = null;
			try {
				tokenAuditId = tokenRef.getAuditIds().get(tokenRef.getAuditIds().size() - 1);
			} catch (Exception e) {
				tokenAuditId = null;
			}

			Calendar expiresAt = tokenRef.getExpires();

			userContext.setExpiresAt(expiresAt);
			userContext.setAuditid(tokenAuditId);
			userContext.setUserid(tokenRef.getUserId());
			userContext.getMethodNames().addAll(tokenRef.getMethods());

			// TODO ignore user_context['extras'].update(token_ref.get('extras',
			// {}))

		} catch (Exception e) {
			logger.error("authenticate failed", e);
			throw Exceptions.UnauthorizedException.getInstance();
		}
	}

	@Override
	public void authenticate(KeystoneContext context, AuthInfo authInfo,
			com.infinities.keystone4j.auth.controller.action.AbstractAuthAction.AuthContext authContext,
			TokenProviderApi tokenProviderApi, IdentityApi identityApi, AssignmentApi assignmentApi) {
		throw Exceptions.NotImplementedException.getInstance();
	}

	@Override
	public String getMethod() {
		return METHOD;
	}
}
