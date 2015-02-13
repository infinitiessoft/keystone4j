package com.infinities.keystone4j.assignment.api.command.user;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;

public class ListUsersForProjectCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<List<String>> {

	private final String projectid;


	public ListUsersForProjectCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String projectid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.projectid = projectid;
	}

	@Override
	public List<String> execute() {
		return this.getAssignmentDriver().listUserIdsForProject(projectid);
	}
}
