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
package com.infinities.keystone4j.identity.controller.action.group;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateGroupAction extends AbstractGroupAction implements ProtectedAction<Group> {

	private final String groupid;
	private final Group group;


	public UpdateGroupAction(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String groupid, Group group) {
		super(identityApi, tokenProviderApi, policyApi);
		this.group = group;
		this.groupid = groupid;
	}

	@Override
	public MemberWrapper<Group> execute(ContainerRequestContext request) throws Exception {
		requireMatchingId(groupid, group);
		Group existedRef = identityApi.getGroup(groupid);
		requireMatchingDomainId(group, existedRef);
		Group ref = this.getIdentityApi().updateGroup(groupid, group);
		return wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "update_group";
	}
}
