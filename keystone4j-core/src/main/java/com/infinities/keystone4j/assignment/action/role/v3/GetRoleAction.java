package com.infinities.keystone4j.assignment.action.role.v3;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;

public class GetRoleAction extends AbstractRoleAction<Role> {

	private String roleid;


	public GetRoleAction(AssignmentApi assignmentApi, String roleid) {
		super(assignmentApi);
		this.roleid = roleid;
	}

	@Override
	public Role execute() {
		return this.getAssignmentApi().getRole(roleid);
	}
}
