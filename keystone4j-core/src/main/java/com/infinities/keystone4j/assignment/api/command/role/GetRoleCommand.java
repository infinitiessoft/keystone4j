package com.infinities.keystone4j.assignment.api.command.role;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;

public class GetRoleCommand extends AbstractAssignmentCommand implements NonTruncatedCommand<Role> {

	private final String roleid;


	public GetRoleCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String roleid) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.roleid = roleid;
	}

	@Override
	public Role execute() {
		return this.getAssignmentDriver().getRole(roleid);
	}
}
