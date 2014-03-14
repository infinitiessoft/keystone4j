package com.infinities.keystone4j.assignment.command.project;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class GetProjectCommand extends AbstractAssignmentCommand<Project> {

	private final String projectid;


	public GetProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String projectid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.projectid = projectid;
	}

	@Override
	public Project execute() {
		return this.getAssignmentDriver().getProject(projectid);
	}
}
