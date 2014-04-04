package com.infinities.keystone4j.assignment.command.project;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.token.TokenApi;

public class GetProjectByNameCommand extends AbstractAssignmentCommand<Project> {

	private final String projectName;
	private final String domainid;


	public GetProjectByNameCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String projectName, String domainid) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.projectName = projectName;
		this.domainid = domainid;
	}

	@Override
	public Project execute() {
		return this.getAssignmentDriver().getProjectByName(projectName, domainid);
	}
}
