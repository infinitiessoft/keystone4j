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
package com.infinities.keystone4j.identity.controller.action.user;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.UserParam;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ChangePasswordAction extends AbstractUserAction implements ProtectedAction<User> {

	private final static Logger logger = LoggerFactory.getLogger(ChangePasswordAction.class);
	private final static String ORIGINAL_PASSWORD = "original_password";
	private final static String USER = "user";
	private final static String PASSWORD = "password";
	private final String userid;
	private final UserParam param;


	// private final Logger logger =
	// LoggerFactory.getLogger(ChangePasswordAction.class);

	public ChangePasswordAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String userid, UserParam param) {
		super(identityApi, tokenProviderApi, policyApi);
		this.param = param;
		this.userid = userid;
	}

	@Override
	public MemberWrapper<User> execute(ContainerRequestContext request) {
		String originalPassword = param.getOriginalPassword();
		if (Strings.isNullOrEmpty(originalPassword)) {
			throw Exceptions.ValidationException.getInstance(null, ORIGINAL_PASSWORD, USER);
		}
		String password = param.getPassword();
		if (Strings.isNullOrEmpty(password)) {
			throw Exceptions.ValidationException.getInstance(null, PASSWORD, USER);
		}

		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);

		try {
			this.getIdentityApi().changePassword(context, userid, originalPassword, password);
		} catch (Exception e) {
			logger.debug("change password failed", e);
			throw Exceptions.UnauthorizedException.getInstance(e);
		}
		return null;
	}

	@Override
	public String getName() {
		return "change_password";
	}
}
