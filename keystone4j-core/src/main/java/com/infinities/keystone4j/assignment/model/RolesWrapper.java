package com.infinities.keystone4j.assignment.model;

import java.util.List;

public class RolesWrapper {

	private List<Role> roles;


	public RolesWrapper(List<Role> roles) {
		super();
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
