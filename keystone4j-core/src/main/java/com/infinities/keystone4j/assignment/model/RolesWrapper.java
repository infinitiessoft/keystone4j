package com.infinities.keystone4j.assignment.model;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;
import com.infinities.keystone4j.common.model.Links;

public class RolesWrapper {

	private List<Role> roles;
	private Links links = new Links();


	public RolesWrapper(List<Role> roles, ContainerRequestContext context) {
		String baseUrl = context.getUriInfo().getBaseUri().toASCIIString() + "v3/roles/";
		this.roles = roles;
		for (Role role : roles) {
			ReferentialLinkUtils.instance.addSelfReferentialLink(role, baseUrl);
		}
		links.setSelf(context.getUriInfo().getRequestUri().toASCIIString());
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}
}
