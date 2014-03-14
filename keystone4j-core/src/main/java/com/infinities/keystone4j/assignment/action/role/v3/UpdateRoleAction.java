package com.infinities.keystone4j.assignment.action.role.v3;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;

public class UpdateRoleAction extends AbstractRoleAction<Role> {

	private String roleid;
	private Role role;


	public UpdateRoleAction(AssignmentApi assignmentApi, String roleid, Role role) {
		super(assignmentApi);
	}

	@Override
	public Role execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(roleid, role);
		return this.getAssignmentApi().updateRole(roleid, role);
	}

	@Override
	public String getName() {
		return "update_role";
	}
}
