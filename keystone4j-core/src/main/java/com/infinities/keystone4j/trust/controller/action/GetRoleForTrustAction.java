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
package com.infinities.keystone4j.trust.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.action.role.v3.AbstractRoleAction;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.wrapper.RoleWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RolesWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class GetRoleForTrustAction extends AbstractTrustAction<Role> implements ProtectedAction<Role> {

	private final String trustid;
	private final String roleid;


	public GetRoleForTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, String trustid, String roleid) {
		super(assignmentApi, identityApi, trustApi, tokenProviderApi, policyApi);
		this.trustid = trustid;
		this.roleid = roleid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) throws Exception {
		checkRoleForTrust(request, trustid, roleid);
		Role role = assignmentApi.getRole(roleid);
		return new AbstractRoleAction(assignmentApi, tokenProviderApi, policyApi) {

			@Override
			public String getName() {
				return null;
			}

		}.wrapMember(request, role);
	}

	@Override
	public String getName() {
		return "get_role_for_trust";
	}

	@Override
	public CollectionWrapper<Role> getCollectionWrapper() {
		return new RolesWrapper();
	}

	@Override
	public MemberWrapper<Role> getMemberWrapper() {
		return new RoleWrapper();
	}
}
