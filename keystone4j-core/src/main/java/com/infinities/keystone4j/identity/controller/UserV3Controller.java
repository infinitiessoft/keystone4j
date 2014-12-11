package com.infinities.keystone4j.identity.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.UserParam;

public interface UserV3Controller {

	MemberWrapper<User> createUser(User user) throws Exception;

	CollectionWrapper<User> listUsers() throws Exception;

	CollectionWrapper<User> listUsersInGroup(String groupid) throws Exception;

	MemberWrapper<User> getUser(String userid) throws Exception;

	MemberWrapper<User> updateUser(String userid, User user) throws Exception;

	void addUserToGroup(String groupid, String userid) throws Exception;

	void checkUserInGroup(String groupid, String userid) throws Exception;

	void removeUserFromGroup(String groupid, String userid) throws Exception;

	void deleteUser(String userid) throws Exception;

	void changePassword(String userid, UserParam user) throws Exception;
}
