package com.infinities.keystone4j.assignment.api.command.role;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.token.TokenApi;

public class ListRolesCommand extends AbstractAssignmentCommand<List<Role>> {

	public ListRolesCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
	}

	@Override
	public List<Role> execute() {
		return this.getAssignmentDriver().listRoles();
	}
}
