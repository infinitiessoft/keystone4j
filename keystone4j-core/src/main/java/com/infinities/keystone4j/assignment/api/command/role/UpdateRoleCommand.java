package com.infinities.keystone4j.assignment.api.command.role;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class UpdateRoleCommand extends AbstractAssignmentCommand implements NotifiableCommand<Role> {

	private final String roleid;
	private final Role role;


	public UpdateRoleCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String roleid, Role role) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.roleid = roleid;
		this.role = role;
	}

	@Override
	public Role execute() {
		Role ret = this.getAssignmentDriver().updateRole(roleid, role);
		// invalidate cache(getRole, getRoleByName)

		return ret;
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return roleid;
		} else if (index == 2) {
			return role;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
