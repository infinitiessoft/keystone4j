package com.infinities.keystone4j.model.assignment;

import com.infinities.keystone4j.model.MemberWrapper;

public class RoleAssignmentWrapper implements MemberWrapper<RoleAssignment> {

	private RoleAssignment roleAssignment;


	public RoleAssignmentWrapper() {

	}

	public RoleAssignmentWrapper(RoleAssignment roleAssignment) {
		this.roleAssignment = roleAssignment;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(roleAssignment,
		// baseUrl);
	}

	public RoleAssignment getRoleAssignment() {
		return roleAssignment;
	}

	public void setRoleAssignment(RoleAssignment roleAssignment) {
		this.roleAssignment = roleAssignment;
	}

	@Override
	public void setRef(RoleAssignment ref) {
		this.roleAssignment = ref;
	}

}
