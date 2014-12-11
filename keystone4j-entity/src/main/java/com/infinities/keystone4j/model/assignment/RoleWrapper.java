package com.infinities.keystone4j.model.assignment;

import com.infinities.keystone4j.model.MemberWrapper;

public class RoleWrapper implements MemberWrapper<Role> {

	private Role role;


	public RoleWrapper() {

	}

	public RoleWrapper(Role role) {
		this.role = role;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(role,
		// baseUrl);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public void setRef(Role ref) {
		this.role = ref;
	}
}
