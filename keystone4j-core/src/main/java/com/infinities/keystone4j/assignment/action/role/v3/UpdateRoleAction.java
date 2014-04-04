package com.infinities.keystone4j.assignment.action.role.v3;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.assignment.Role;

public class UpdateRoleAction extends AbstractRoleAction<Role> {

	private final String roleid;
	private final Role role;


	public UpdateRoleAction(AssignmentApi assignmentApi, String roleid, Role role) {
		super(assignmentApi);
		this.role = role;
		this.roleid = roleid;
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
