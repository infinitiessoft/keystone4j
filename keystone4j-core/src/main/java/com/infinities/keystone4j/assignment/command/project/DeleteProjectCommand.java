package com.infinities.keystone4j.assignment.command.project;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.TokenApi;

public class DeleteProjectCommand extends AbstractAssignmentCommand<Project> {

	private final String projectid;


	public DeleteProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String projectid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.projectid = projectid;
	}

	@Override
	public Project execute() {
		// verify project exist
		this.getAssignmentDriver().getProject(projectid);
		List<User> users = this.getAssignmentDriver().listUsersForProject(projectid);
		for (User user : users) {
			this.getTokenApi().deleteTokensForUser(user.getId(), projectid);
		}

		this.getAssignmentDriver().deleteProject(projectid);
		// invalidate cache(getProject, getProjectByName)
		this.getCredentialApi().deleteCredentialsForProject(projectid);

		return null;
	}
}
