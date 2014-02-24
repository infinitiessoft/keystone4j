package com.infinities.keystone4j.identity.controller;

import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.identity.model.UserParam;
import com.infinities.keystone4j.identity.model.UserWrapper;
import com.infinities.keystone4j.identity.model.UsersWrapper;

public interface UserV3Controller {

	UserWrapper createUser(User user);

	UsersWrapper listUsers(String domainid, String email, Boolean enabled, String name, int page, int perPage);

	UsersWrapper listUsersInGroup(String groupid, String domainid, String email, Boolean enabled, String name, int page,
			int perPage);

	UserWrapper getUser(String userid);

	UserWrapper updateUser(String userid, User user);

	void addUserToGroup(String groupid, String userid);

	void checkUserInGroup(String groupid, String userid);

	void removeUserFromGroup(String groupid, String userid);

	void deleteUser(String userid);

	void changePassword(String userid, UserParam user);
}
