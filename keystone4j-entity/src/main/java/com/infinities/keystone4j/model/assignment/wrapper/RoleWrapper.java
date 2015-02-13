package com.infinities.keystone4j.model.assignment.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;

public class RoleWrapper implements MemberWrapper<Role> {

	private Role role;


	public RoleWrapper() {

	}

	public RoleWrapper(Role role) {
		this.role = role;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(role,
		// baseUrl);
	}

	@Override
	public void setRef(Role ref) {
		this.role = ref;
	}

	@XmlElement(name = "role")
	@Override
	public Role getRef() {
		return role;
	}
}
