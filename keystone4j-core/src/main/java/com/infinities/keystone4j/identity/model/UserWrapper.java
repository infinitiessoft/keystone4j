package com.infinities.keystone4j.identity.model;

public class UserWrapper {

	private User user;


	public UserWrapper() {

	}

	public UserWrapper(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
