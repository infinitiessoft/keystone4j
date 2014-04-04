package com.infinities.keystone4j.identity.controller;

import com.infinities.keystone4j.model.identity.UserWrapper;
import com.infinities.keystone4j.model.identity.UsersWrapper;

public interface UserController {

	UserWrapper getUser(String userid);

	UsersWrapper getUsers();

	UserWrapper getUserByName();

	UserWrapper createUser();

	UserWrapper updateUser();

	void deleteUser();

	UserWrapper setUserEnabled();

	UserWrapper setUserPassword();

}
