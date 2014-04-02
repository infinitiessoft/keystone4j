package com.infinities.keystone4j.identity.model;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;

public class UserWrapper {

	private User user;


	public UserWrapper() {

	}

	public UserWrapper(User user, ContainerRequestContext context) {
		this(user, context.getUriInfo().getBaseUri().toASCIIString() + "v3/users/");
	}

	public UserWrapper(User user, String baseUrl) {
		this.user = user;
		ReferentialLinkUtils.instance.addSelfReferentialLink(user, baseUrl);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
