package com.infinities.keystone4j.assignment.command.project;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class ListProjectsCommand extends AbstractAssignmentCommand<List<Project>> {

	public ListProjectsCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
	}

	@Override
	public List<Project> execute() {
		return this.getAssignmentDriver().listProjects(null);
	}
}
