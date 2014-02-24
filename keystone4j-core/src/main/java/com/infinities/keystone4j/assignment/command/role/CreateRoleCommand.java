package com.infinities.keystone4j.assignment.command.role;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class CreateRoleCommand extends AbstractAssignmentCommand<Role> {

	private Role role;


	public CreateRoleCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, Role role) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.role = role;
	}

	@Override
	public Role execute() {
		return this.getAssignmentDriver().createRole(role);
	}

}
