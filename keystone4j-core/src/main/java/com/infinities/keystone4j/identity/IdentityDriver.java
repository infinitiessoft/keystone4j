/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
