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
package com.infinities.keystone4j.identity.controller.callback;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.Callback;
import com.infinities.keystone4j.ControllerAction;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CheckUserAndGroupProtection extends ControllerAction implements Callback {

	private final String groupid;
	private final String userid;
	private final IdentityApi identityApi;


	public CheckUserAndGroupProtection(String userid, String groupid, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.userid = userid;
		this.groupid = groupid;
		this.identityApi = identityApi;

	}

	@Override
	public void execute(ContainerRequestContext request, ProtectedAction<?> command) throws Exception {
		// Map<String, Object> ref = Maps.newHashMap();
		Target ref = new Target();
		User user = this.identityApi.getUser(userid);
		ref.setUser(user);
		// ref.put("user", user);
		Group group = this.identityApi.getGroup(groupid);
		ref.setGroup(group);
		// ref.put("group", group);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		checkProtection(context, request, command, ref);
	}

}
