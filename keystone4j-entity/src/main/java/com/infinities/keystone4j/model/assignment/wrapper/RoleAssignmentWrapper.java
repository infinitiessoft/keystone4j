package com.infinities.keystone4j.model.assignment.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.RoleAssignment;

public class RoleAssignmentWrapper implements MemberWrapper<RoleAssignment> {

	private RoleAssignment roleAssignment;


	public RoleAssignmentWrapper() {

	}

	public RoleAssignmentWrapper(RoleAssignment roleAssignment) {
		this.roleAssignment = roleAssignment;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(roleAssignment,
		// baseUrl);
	}

	@Override
	public void setRef(RoleAssignment ref) {
		this.roleAssignment = ref;
	}

	@XmlElement(name = "roleAssignments")
	@Override
	public RoleAssignment getRef() {
		return roleAssignment;
	}

}
