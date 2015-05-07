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
package com.infinities.keystone4j.assignment.api.command.role;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Metadata;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;

public class GetRolesForUserAndProjectCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<List<String>> {

	private final String userid;
	private final String tenanid;
	private final static String ENABLED = "enabled";


	public GetRolesForUserAndProjectCommand(CredentialApi credentialApi, IdentityApi identityApi,
			AssignmentApi assignmentApi, RevokeApi revokeApi, AssignmentDriver assignmentDriver, String userid,
			String tenantid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.userid = userid;
		this.tenanid = tenantid;
	}

	@Override
	public List<String> execute() throws Exception {
		Project projectRef = this.getAssignmentApi().getProject(tenanid);
		List<String> userRoleList = getUserProjectRoles(userid, projectRef);
		List<String> groupRoleList = getGroupProjectRoles(userid, projectRef);

		Set<String> set = Sets.newHashSet();
		set.addAll(userRoleList);
		set.addAll(groupRoleList);

		return Lists.newArrayList(set);
	}

	private List<String> getGroupProjectRoles(String userid, Project project) throws Exception {
		List<String> groupids = getGroupIdsForUserId(userid);
		return this.getAssignmentDriver().getGroupProjectRoles(groupids, project.getId(), project.getDomainId());
	}

	private List<String> getUserProjectRoles(String userid, Project project) throws Exception {
		List<String> roleList = Lists.newArrayList();
		// _get_metadata
		try {
			Metadata metadataRef = this.getAssignmentDriver().getMetadata(userid, project.getId(), null, null);
			roleList = this.getAssignmentDriver().rolesFromRoleDicts(metadataRef.getRoles(), false);
		} catch (Exception e) {
			// no group grant, skip
		}

		boolean enabled = Config.Instance.getOpt(Config.Type.os_inherit, ENABLED).asBoolean();
		if (enabled) {
			try {
				Metadata metadataRef = this.getAssignmentDriver().getMetadata(userid, null, null, project.getDomainId());
				roleList.addAll(this.getAssignmentDriver().rolesFromRoleDicts(metadataRef.getRoles(), true));
			} catch (Exception e) {
				// ignore
			}
			for (Project p : this.getAssignmentApi().listProjectParents(project.getId(), null)) {
				List<Role> pRoles = this.getAssignmentApi().listGrants(userid, null, null, p.getId(), true);
				for (Role role : pRoles) {
					roleList.add(role.getId());
				}
			}
		}

		return roleList;
	}
}
