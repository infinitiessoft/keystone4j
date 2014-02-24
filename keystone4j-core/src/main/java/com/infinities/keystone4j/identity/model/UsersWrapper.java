package com.infinities.keystone4j.identity.model;

import java.util.List;

public class UsersWrapper {

	private List<User> users;


	public UsersWrapper(List<User> users) {
		super();
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
