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
package com.infinities.keystone4j.assignment.controller.action.grant;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteGrantAction extends AbstractGrantAction implements ProtectedAction<Role> {

	private final String userid;
	private final String roleid;
	private final String groupid;
	private final String domainid;
	private final String projectid;


	public DeleteGrantAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi, String roleid, String userid, String groupid, String domainid, String projectid) {
		super(assignmentApi, identityApi, tokenProviderApi, policyApi);
		this.userid = userid;
		this.roleid = roleid;
		this.groupid = groupid;
		this.domainid = domainid;
		this.projectid = projectid;
	}

	@Override
	public MemberWrapper<Role> execute(ContainerRequestContext request) throws Exception {
		requireDomainXorProject(domainid, projectid);
		requireUserXorGroup(userid, groupid);
		assignmentApi.deleteGrant(roleid, userid, groupid, domainid, projectid, checkIfInherited(request));
		return null;
	}

	@Override
	public String getName() {
		return "revoke_grant";
	}
}
