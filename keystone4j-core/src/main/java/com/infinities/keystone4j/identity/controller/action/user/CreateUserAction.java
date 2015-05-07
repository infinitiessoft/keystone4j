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

import com.google.common.base.Strings;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateUserAction extends AbstractUserAction implements ProtectedAction<User> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(CreateUserAction.class);
	private final User user;


	public CreateUserAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, User user) {
		super(identityApi, tokenProviderApi, policyApi);
		this.user = user;
	}

	@Override
	public MemberWrapper<User> execute(ContainerRequestContext request) throws Exception {
		// require_attribute(user, 'name')
		if (Strings.isNullOrEmpty(user.getName())) {
			String msg = String.format("%s field is required and cannot be empty", "name");
			throw Exceptions.ValidationException.getInstance(msg);
		}
		// ignore normalize_dict(user)
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		normalizeDomainid(context, user);
		User ret = identityApi.createUser(user);
		return wrapMember(request, ret);
	}

	@Override
	public String getName() {
		return "create_user";
	}
}
