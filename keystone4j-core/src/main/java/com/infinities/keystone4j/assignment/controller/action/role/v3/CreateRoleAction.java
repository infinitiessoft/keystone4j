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
package com.infinities.keystone4j.assignment.controller.action.role.v3;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateRoleAction extends AbstractRoleAction implements ProtectedAction<Role> {

	private final Role role;


	public CreateRoleAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi, Role role) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.role = role;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) throws Exception {
		assignUniqueId(role);
		Role ref = assignmentApi.createRole(role.getId(), role);
		return this.wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "create_role";
	}
}
