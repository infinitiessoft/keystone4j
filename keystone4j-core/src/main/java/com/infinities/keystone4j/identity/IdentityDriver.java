package com.infinities.keystone4j.identity;

import java.util.List;

import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;

public interface IdentityDriver {

	User authenticate(String userid, String password);

	User createUser(User user);

	List<User> listUsers();

	List<User> listUsersInGroup(String groupid);

	User getUser(String userid);

	User updateUser(String userid, User user);

	void addUserToGroup(String userid, String groupid);

	void checkUserInGroup(String userid, String groupid);

	void removeUserFromGroup(String userid, String groupid);

	void deleteUser(String userid);

	User getUserByName(String userName, String domainid);

	Group createGroup(Group group);

	List<Group> listGroups();

	List<Group> listGroupsForUser(String userid);

	Group getGroup(String groupid);

	Group updateGroup(String groupid, Group group);

	boolean isDomainAware();

	void deleteGroup(String groupid);

}
