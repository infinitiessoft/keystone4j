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

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;

public class RemoveRoleFromUserAndProjectCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Role> {

	private final String userid;
	private final String tenantid;
	private final String roleid;


	public RemoveRoleFromUserAndProjectCommand(CredentialApi credentialApi, IdentityApi identityApi,
			AssignmentApi assignmentApi, RevokeApi revokeApi, AssignmentDriver assignmentDriver, String userid,
			String tenantid, String roleid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.userid = userid;
		this.tenantid = tenantid;
		this.roleid = roleid;
	}

	@Override
	public Role execute() throws Exception {
		this.getAssignmentDriver().removeRoleFromUserAndProject(userid, tenantid, roleid);
		this.getIdentityApi().emitInvalidateUserTokenPersistence(userid);
		if (this.getRevokeApi() != null) {
			this.getRevokeApi().revokeByGrant(roleid, userid, null, tenantid);
		}
		return null;
	}
}
