package com.infinities.keystone4j.assignment.action.role.v3;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.assignment.Role;

public class GetRoleAction extends AbstractRoleAction<Role> {

	private final String roleid;


	public GetRoleAction(AssignmentApi assignmentApi, String roleid) {
		super(assignmentApi);
		this.roleid = roleid;
	}

	@Override
	public Role execute(ContainerRequestContext request) {
		return this.getAssignmentApi().getRole(roleid);
	}

	@Override
	public String getName() {
		return "get_role";
	}
}
