package com.infinities.keystone4j.assignment.command.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class AddUserToProjectCommand extends AbstractAssignmentCommand<Project> {

	private final static Logger logger = LoggerFactory.getLogger(AddUserToProjectCommand.class);
	private final static String ROLE_NOT_FOUND = "Creating the default role {} because it does not exist.";
	private final static String MEMBER_ROLE_ID = "member_role_id";
	private final String projectid;
	private final String userid;


	public AddUserToProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String projectid, String userid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.projectid = projectid;
		this.userid = userid;
	}

	@Override
	public Project execute() {
		String roleid = Config.Instance.getOpt(Config.Type.DEFAULT, MEMBER_ROLE_ID).asText();
		try {
			this.getAssignmentDriver().addRoleToUserAndProject(userid, projectid, roleid);
		} catch (Exception e) {
			logger.info(ROLE_NOT_FOUND, roleid);
		}

		return null;
	}
}
