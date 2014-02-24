package com.infinities.keystone4j.assignment.command.project;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.token.TokenApi;

public class UpdateProjectCommand extends AbstractAssignmentCommand<Project> {

	private final String projectid;
	private final Project project;


	public UpdateProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String projectid, Project project) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.projectid = projectid;
		this.project = project;
	}

	@Override
	public Project execute() {

		if (!project.getEnabled()) {
			List<User> users = this.getAssignmentDriver().listUsersForProject(projectid);
			for (User user : users) {
				this.getTokenApi().deleteTokensForUser(user.getId(), projectid);
			}
		}

		Project ret = this.getAssignmentDriver().updateProject(projectid, project);
		// TODO invalidate cache(getProject, getProjectByName)

		return ret;
	}
}
