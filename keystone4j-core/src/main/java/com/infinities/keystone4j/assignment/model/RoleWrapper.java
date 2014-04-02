package com.infinities.keystone4j.assignment.model;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;

public class RoleWrapper {

	private Role role;


	public RoleWrapper() {

	}

	public RoleWrapper(Role role, ContainerRequestContext context) {
		this(role, context.getUriInfo().getBaseUri().toASCIIString() + "v3/roles/");
	}

	public RoleWrapper(Role role, String baseUrl) {
		this.role = role;
		ReferentialLinkUtils.instance.addSelfReferentialLink(role, baseUrl);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
