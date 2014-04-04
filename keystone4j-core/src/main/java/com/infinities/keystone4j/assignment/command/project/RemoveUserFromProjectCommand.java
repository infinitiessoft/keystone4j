package com.infinities.keystone4j.assignment.command.project;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.TokenApi;

public class RemoveUserFromProjectCommand extends AbstractAssignmentCommand<Project> {

	private final static Logger logger = LoggerFactory.getLogger(RemoveUserFromProjectCommand.class);
	private final static String ROLE_NOT_FOUND = "Removing role {} failed because it does not exist.";
	private final String projectid;
	private final String userid;


	public RemoveUserFromProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String projectid, String userid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.projectid = projectid;
		this.userid = userid;
	}

	@Override
	public Project execute() {
		List<Role> roles = this.getAssignmentApi().getRolesForUserAndProject(userid, projectid);
		if (roles == null) {
			throw Exceptions.NotFoundException.getInstance(null, projectid);
		}
		for (Role role : roles) {
			try {
				this.getAssignmentDriver().removeRoleFromUserAndProject(userid, projectid, role.getId());
			} catch (Exception e) {
				logger.debug(ROLE_NOT_FOUND, role.getId());
			}
		}

		return null;
	}
}
