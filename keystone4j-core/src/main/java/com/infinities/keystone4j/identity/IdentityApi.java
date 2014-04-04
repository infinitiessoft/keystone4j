package com.infinities.keystone4j.identity;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;

public interface IdentityApi extends Api {

	User authenticate(String userid, String password, String domainid);

	User getUser(String userid, String domainid);

	Group getGroup(String groupid, String domainid);

	Group createGroup(Group group);

	List<Group> listGroups(String domainid);

	Group updateGroup(String groupid, Group group, String domainid);

	Group deleteGroup(String groupid, String domainid);

	List<Group> listGroupsForUser(String userid, String domainid);

	User createUser(User user);

	List<User> listUsers(String id);

	User updateUser(String userid, User user, String domainid);

	User deleteUser(String userid, String id);

	List<User> listUsersInGroup(String groupid, String domainid);

	User removeUserFromGroup(String userid, String groupid, String domainid);

	User checkUserInGroup(String userid, String groupid, String domainid);

	User addUserToGroup(String userid, String groupid, String domainid);

	User getUserByName(String userName, String domainid);

}
