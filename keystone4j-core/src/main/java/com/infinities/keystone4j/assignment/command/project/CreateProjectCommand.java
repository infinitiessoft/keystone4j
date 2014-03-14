package com.infinities.keystone4j.assignment.command.project;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class CreateProjectCommand extends AbstractAssignmentCommand<Project> {

	private final Project project;


	public CreateProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, Project project) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.project = project;
	}

	@Override
	public Project execute() {
		return this.getAssignmentDriver().createProject(project);
	}

}
