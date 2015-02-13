package com.infinities.keystone4j.assignment.api.command.project;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;

public class GetProjectByNameCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Project> {

	private final String projectName;
	private final String domainid;


	public GetProjectByNameCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String projectName, String domainid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.projectName = projectName;
		this.domainid = domainid;
	}

	@Override
	public Project execute() {
		return this.getAssignmentDriver().getProjectByName(projectName, domainid);
	}
}
