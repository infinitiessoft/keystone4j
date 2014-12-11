package com.infinities.keystone4j.identity;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;

public interface IdentityApi extends Api {

	User authenticate(String userid, String password, String domainid);

	User getUser(String userid);

	Group getGroup(String groupid);

	Group createGroup(Group group);

	List<Group> listGroups(String domainid, Hints hints);

	Group updateGroup(String groupid, Group group);

	Group deleteGroup(String groupid);

	List<Group> listGroupsForUser(String userid, Hints hints);

	User createUser(User user);

	List<User> listUsers(String domainScope, Hints hints);

	User updateUser(String userid, User user);

	User deleteUser(String userid);

	List<User> listUsersInGroup(String groupid, Hints hints);

	User removeUserFromGroup(String userid, String groupid);

	User checkUserInGroup(String userid, String groupid);

	User addUserToGroup(String userid, String groupid);

	User getUserByName(String userName, String domainid);

	void changePassword(KeystoneContext context, String userid, String originalPassword, String password);

}
