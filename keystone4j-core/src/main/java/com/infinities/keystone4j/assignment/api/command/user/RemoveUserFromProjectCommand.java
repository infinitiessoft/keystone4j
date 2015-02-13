package com.infinities.keystone4j.assignment.api.command.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;

public class RemoveUserFromProjectCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Project> {

	private final static Logger logger = LoggerFactory.getLogger(RemoveUserFromProjectCommand.class);
	private final static String ROLE_NOT_FOUND = "Removing role {} failed because it does not exist.";
	private final String projectid;
	private final String userid;


	public RemoveUserFromProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String projectid, String userid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.projectid = projectid;
		this.userid = userid;
	}

	@Override
	public Project execute() throws Exception {
		List<String> roles = this.getAssignmentApi().getRolesForUserAndProject(userid, projectid);
		if (roles == null) {
			throw Exceptions.NotFoundException.getInstance(null, projectid);
		}
		for (String roleId : roles) {
			try {
				this.getAssignmentDriver().removeRoleFromUserAndProject(userid, projectid, roleId);

				if (this.getRevokeApi() != null) {
					this.getRevokeApi().revokeByGrant(roleId, userid, null, projectid);
				}
			} catch (Exception e) {
				logger.debug(ROLE_NOT_FOUND, roleId);
			}
		}

		return null;
	}
}
