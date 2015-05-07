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

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;

public interface IdentityApi extends Api {

	User authenticate(String userid, String password) throws Exception;

	User getUser(String userid) throws Exception;

	Group getGroup(String groupid) throws Exception;

	Group getGroupByName(String groupName, String domainid) throws Exception;

	Group createGroup(Group group) throws Exception;

	List<Group> listGroups(String domainid, Hints hints) throws Exception;

	Group updateGroup(String groupid, Group group) throws Exception;

	Group deleteGroup(String groupid) throws Exception;

	List<Group> listGroupsForUser(String userid, Hints hints) throws Exception;

	User createUser(User user) throws Exception;

	List<User> listUsers(String domainScope, Hints hints) throws Exception;

	User updateUser(String userid, User user) throws Exception;

	User deleteUser(String userid) throws Exception;

	List<User> listUsersInGroup(String groupid, Hints hints) throws Exception;

	User removeUserFromGroup(String userid, String groupid) throws Exception;

	User checkUserInGroup(String userid, String groupid) throws Exception;

	User addUserToGroup(String userid, String groupid) throws Exception;

	User getUserByName(String userName, String domainid) throws Exception;

	void changePassword(KeystoneContext context, String userid, String originalPassword, String password) throws Exception;

	void emitInvalidateUserTokenPersistence(String userid) throws Exception;

	boolean isMultipleDomainsSupported() throws Exception;

	// user=null
	void assertUserEnabled(String id, User user) throws Exception;

	void setAssignmentApi(AssignmentApi assignmentApi);

}
