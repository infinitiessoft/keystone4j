package com.infinities.keystone4j.assignment.action.role.v3;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;

public class CreateRoleAction extends AbstractRoleAction<Role> {

	private final Role role;


	public CreateRoleAction(AssignmentApi assignmentApi, Role role) {
		super(assignmentApi);
		this.role = role;
	}

	@Override
	public Role execute() {
		Role ret = assignmentApi.createRole(role);
		return ret;
	}

	@Override
	public String getName() {
		return "create_role";
	}
}
