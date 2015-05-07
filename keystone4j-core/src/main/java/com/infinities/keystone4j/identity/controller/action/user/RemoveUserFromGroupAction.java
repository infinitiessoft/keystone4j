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

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class RemoveUserFromGroupAction extends AbstractUserAction implements ProtectedAction<User> {

	private final String userid;
	private final String groupid;


	public RemoveUserFromGroupAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String userid, String groupid) {
		super(identityApi, tokenProviderApi, policyApi);
		this.userid = userid;
		this.groupid = groupid;
	}

	@Override
	public MemberWrapper<User> execute(ContainerRequestContext request) throws Exception {
		this.getIdentityApi().removeUserFromGroup(userid, groupid);
		return null;
	}

	@Override
	public String getName() {
		return "remove_user_from_group";
	}
}
