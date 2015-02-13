package com.infinities.keystone4j.identity;

import java.util.List;

import com.infinities.keystone4j.Driver;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;

public interface IdentityDriver extends Driver {

	User authenticate(String userid, String password);

	User createUser(String userid, User user);

	List<User> listUsers(Hints hints) throws Exception;

	List<User> listUsersInGroup(String groupid, Hints hints);

	User getUser(String userid);

	User updateUser(String userid, User user);

	void addUserToGroup(String userid, String groupid);

	void checkUserInGroup(String userid, String groupid);

	void removeUserFromGroup(String userid, String groupid);

	void deleteUser(String userid);

	User getUserByName(String userName, String domainid);

	Group createGroup(String groupid, Group group);

	List<Group> listGroups(Hints hints) throws Exception;

	List<Group> listGroupsForUser(String userid, Hints hints);

	Group getGroup(String groupid);

	Group updateGroup(String groupid, Group group);

	boolean isDomainAware();

	void deleteGroup(String groupid);

	boolean generateUuids();

	boolean isSql();

	void setAssignmentApi(AssignmentApi assignmentApi);

	Group getGroupByName(String groupName, String domainid);

	boolean isMultipleDomainsSupported();

}
