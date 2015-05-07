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
package com.infinities.keystone4j.assignment.api.command.grant;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;

public class GetGrantCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Role> {

	// private Logger logger =
	// LoggerFactory.getLogger(GetGrantByGroupDomainCommand.class);
	// private final static String GROUP_NOT_FOUND =
	// "Group {} not found, no tokens to invalidate.";
	private final String roleid;
	private final String userid;
	private final String groupid;
	private final String domainid;
	private final String projectid;
	private final boolean inherited;


	public GetGrantCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String roleid, String userid, String groupid,
			String domainid, String projectid, boolean inherited) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.groupid = groupid;
		this.domainid = domainid;
		this.inherited = inherited;
		this.roleid = roleid;
		this.userid = userid;
		this.projectid = projectid;
	}

	@Override
	public Role execute() {
		return this.getAssignmentDriver().getGrant(roleid, userid, groupid, domainid, projectid, inherited);
	}
}
