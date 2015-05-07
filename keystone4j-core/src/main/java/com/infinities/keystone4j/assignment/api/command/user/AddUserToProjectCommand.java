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
package com.infinities.keystone4j.assignment.api.command.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.identity.User;

public class AddUserToProjectCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<User> {

	private final static Logger logger = LoggerFactory.getLogger(AddUserToProjectCommand.class);
	private final String projectid;
	private final String userid;


	public AddUserToProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String projectid, String userid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.projectid = projectid;
		this.userid = userid;
	}

	@Override
	public User execute() {
		try {
			this.getAssignmentDriver().addRoleToUserAndProject(userid, projectid,
					Config.Instance.getOpt(Config.Type.DEFAULT, "member_role_id").asText());
		} catch (Exception e) {
			logger.info("Creating the default role {} because it does not exist",
					Config.Instance.getOpt(Config.Type.DEFAULT, "member_role_id").asText());
			Role role = new Role();
			role.setId(Config.Instance.getOpt(Config.Type.DEFAULT, "member_role_id").asText());
			role.setName(Config.Instance.getOpt(Config.Type.DEFAULT, "member_role_name").asText());
			this.getAssignmentDriver().createRole(Config.Instance.getOpt(Config.Type.DEFAULT, "member_role_id").asText(),
					role);
			this.getAssignmentDriver().addRoleToUserAndProject(userid, projectid,
					Config.Instance.getOpt(Config.Type.DEFAULT, "member_role_id").asText());
		}
		return null;
	}
}
