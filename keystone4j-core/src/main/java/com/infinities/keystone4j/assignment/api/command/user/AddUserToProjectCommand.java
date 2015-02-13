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
