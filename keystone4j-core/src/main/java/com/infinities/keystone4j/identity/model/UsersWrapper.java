package com.infinities.keystone4j.identity.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.utils.jackson.Views;

public class UsersWrapper {

	@JsonView(Views.Basic.class)
	private List<User> users;


	public UsersWrapper(List<User> users) {
		// super();
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
