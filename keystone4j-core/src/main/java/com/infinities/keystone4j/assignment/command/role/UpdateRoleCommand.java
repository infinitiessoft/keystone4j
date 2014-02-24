package com.infinities.keystone4j.assignment.command.role;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class UpdateRoleCommand extends AbstractAssignmentCommand<Role> {

	private String roleid;
	private Role role;


	public UpdateRoleCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver, String roleid, Role role) {
		super(credentialApi, identityApi, tokenApi, assignmentApi, assignmentDriver);
		this.roleid = roleid;
		this.role = role;
	}

	@Override
	public Role execute() {
		Role ret = this.getAssignmentDriver().updateRole(roleid, role);
		// invalidate cache(getRole, getRoleByName)

		return ret;
	}
}
