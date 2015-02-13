package com.infinities.keystone4j.assignment.api.command.project;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;

public class ListProjectsInDomainCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<List<Project>> {

	private final String domainid;


	public ListProjectsInDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String domainid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.domainid = domainid;
	}

	@Override
	public List<Project> execute() throws Exception {
		return this.getAssignmentDriver().listProjectsInDomain(domainid);
	}
}
