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
