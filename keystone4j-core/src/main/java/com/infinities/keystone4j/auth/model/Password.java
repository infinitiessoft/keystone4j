package com.infinities.keystone4j.auth.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.infinities.keystone4j.identity.model.User;

public class Password implements AuthData {

	private User user;


	@Override
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@JsonIgnore
	@Override
	public String getId() {
		return null;
	}

}
