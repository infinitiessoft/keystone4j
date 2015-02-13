package com.infinities.keystone4j.assignment.api.command.role;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Assignment;

public class ListRoleAssignmentsCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<List<Assignment>> {

	public ListRoleAssignmentsCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
	}

	@Override
	public List<Assignment> execute() {
		List<Assignment> assignments = this.getAssignmentDriver().listRoleAssignments();
		return assignments;
	}
}
