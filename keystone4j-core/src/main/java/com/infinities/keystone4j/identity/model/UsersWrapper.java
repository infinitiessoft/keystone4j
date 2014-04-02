package com.infinities.keystone4j.identity.model;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.ReferentialLinkUtils;
import com.infinities.keystone4j.common.model.Links;
import com.infinities.keystone4j.utils.jackson.Views;

public class UsersWrapper {

	@JsonView(Views.Basic.class)
	private List<User> users;
	private Links links = new Links();


	public UsersWrapper(List<User> users, ContainerRequestContext context) {
		String baseUrl = context.getUriInfo().getBaseUri().toASCIIString() + "v3/users/";
		this.users = users;
		for (User user : users) {
			ReferentialLinkUtils.instance.addSelfReferentialLink(user, baseUrl);
		}
		links.setSelf(context.getUriInfo().getRequestUri().toASCIIString());
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}

}
