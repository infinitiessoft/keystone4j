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
package com.infinities.keystone4j.identity.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.ListFunction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.common.TruncatedFunction;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.driver.function.ListGroupsFunction;
import com.infinities.keystone4j.identity.driver.function.ListUsersFunction;
import com.infinities.keystone4j.jpa.impl.GroupDao;
import com.infinities.keystone4j.jpa.impl.UserDao;
import com.infinities.keystone4j.jpa.impl.UserGroupMembershipDao;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.UserGroupMembership;
import com.infinities.keystone4j.utils.PasswordUtils;

public class IdentityJpaDriver implements IdentityDriver {

	private final static Logger logger = LoggerFactory.getLogger(IdentityJpaDriver.class);
	private final UserDao userDao;
	private final GroupDao groupDao;
	private final UserGroupMembershipDao userGroupMembershipDao;
	private final static String USER_NOT_FOUND = "User %s not found in group %s";
	private final static String INVALID_USER_PASSWORD = "Invalid user / password";


	public IdentityJpaDriver() {
		super();
		this.userDao = new UserDao();
		this.groupDao = new GroupDao();
		this.userGroupMembershipDao = new UserGroupMembershipDao();
	}

	@Override
	public boolean isSql() {
		return true;
	}

	private boolean checkPassword(String password, User userRef) {
		return PasswordUtils.checkPassword(password, userRef.getPassword());
	}

	private User filterUser(User userRef) {
		if (userRef != null) {
			// userRef.setPassword(null);
		}
		return userRef;
	}

	@Override
	public User authenticate(String userid, String password) {
		User userRef = null;
		try {
			userRef = _getUser(userid);
		} catch (Exception e) {
			logger.debug("get user failed", e);
			// replace AssertionError
			throw new IllegalArgumentException(INVALID_USER_PASSWORD);
		}
		logger.debug("_getUser : {}", userRef.toString());
		boolean checked = checkPassword(password, userRef);
		if (!checked) {
			// replace AssertionError
			throw new IllegalArgumentException(INVALID_USER_PASSWORD);
		}
		return filterUser(userRef);
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='user')
	@Override
	public User createUser(String userid, User user) {
		user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
		userDao.persist(user);
		return user;
	}

	@Override
	public List<User> listUsers(Hints hints) throws Exception {
		ListFunction<User> function = new TruncatedFunction<User>(new ListUsersFunction());
		function.execute(hints);
		List<User> refs = new ArrayList<User>();
		for (User userRef : function.execute(hints)) {
			refs.add(filterUser(userRef));
		}
		return refs;
	}

	@Override
	public User getUser(String userid) {
		User userRef = _getUser(userid);
		// return filterUser(userRef);
		return userRef;
	}

	private User _getUser(String userid) {
		User userRef = userDao.findById(userid);
		if (userRef == null) {
			throw Exceptions.UserNotFoundException.getInstance(null, userid);
		}

		return userRef;
	}

	@Override
	public User getUserByName(String userName, String domainid) {
		try {
			User user = userDao.findByName(userName, domainid);
			return user;
		} catch (NoResultException e) {
			throw Exceptions.UserNotFoundException.getInstance(null, userName);
		}
	}

	@Override
	public User updateUser(String userid, User user) {
		User oldUser = _getUser(userid);
		if (user.isDefaultProjectUpdated()) {
			oldUser.setDefaultProject(user.getDefaultProject());
		}
		if (user.isDescriptionUpdated()) {
			oldUser.setDescription(user.getDescription());
		}
		if (user.isDomainUpdated()) {
			oldUser.setDomain(user.getDomain());
		}
		if (user.isEnabledUpdated()) {
			oldUser.setEnabled(user.getEnabled());
		}
		if (user.isExtraUpdated()) {
			oldUser.setExtra(user.getExtra());
		}
		if (user.isNameUpdated()) {
			oldUser.setName(user.getName());
		}
		if (user.isPasswordUpdated()) {
			user = PasswordUtils.hashUserPassword(user);
			oldUser.setPassword(user.getPassword());
		}

		return filterUser(userDao.merge(oldUser));
	}

	@Override
	public void addUserToGroup(String userid, String groupid) {
		Group group = getGroup(groupid);
		User user = getUser(userid);
		try {
			userGroupMembershipDao.findByUserGroup(userid, groupid);
			return;
		} catch (NoResultException e) {
			UserGroupMembership membership = new UserGroupMembership();
			membership.setGroup(group);
			membership.setUser(user);
			membership.setId(UUID.randomUUID().toString());
			userGroupMembershipDao.persist(membership);
		}
	}

	@Override
	public void checkUserInGroup(String userid, String groupid) {
		getGroup(groupid);
		getUser(userid);
		try {
			userGroupMembershipDao.findByUserGroup(userid, groupid);
			return;
		} catch (NoResultException e) {
			String msg = String.format(USER_NOT_FOUND, userid, groupid);
			throw Exceptions.NotFoundException.getInstance(msg);
		}
	}

	@Override
	public void removeUserFromGroup(String userid, String groupid) {
		try {
			UserGroupMembership membership = userGroupMembershipDao.findByUserGroup(userid, groupid);
			userGroupMembershipDao.remove(membership);
		} catch (NoResultException e) {
			String msg = String.format(USER_NOT_FOUND, userid, groupid);
			throw Exceptions.NotFoundException.getInstance(msg);
		}
	}

	@Override
	public List<Group> listGroupsForUser(String userid, Hints hints) {
		getUser(userid);
		List<Group> groups = userGroupMembershipDao.listGroupsByUser(userid);
		return groups;
	}

	@Override
	public List<User> listUsersInGroup(String groupid, Hints hints) {
		getGroup(groupid);
		List<User> users = userGroupMembershipDao.listUserByGroup(groupid);
		List<User> refs = new ArrayList<User>();
		for (User user : users) {
			refs.add(filterUser(user));
		}

		return refs;
	}

	@Override
	public void deleteUser(String userid) {
		User user = _getUser(userid);
		List<UserGroupMembership> memberships = userGroupMembershipDao.listByUser(userid);
		for (UserGroupMembership userGroupMembership : memberships) {
			userGroupMembershipDao.remove(userGroupMembership);
		}
		userDao.remove(user);
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='group')
	@Override
	public Group createGroup(String groupid, Group group) {
		groupDao.persist(group);
		return group;
	}

	@Override
	public List<Group> listGroups(Hints hints) throws Exception {
		ListFunction<Group> function = new TruncatedFunction<Group>(new ListGroupsFunction());
		return function.execute(hints);
	}

	@Override
	public Group getGroup(String groupid) {
		Group group = groupDao.findById(groupid);
		if (group == null) {
			throw Exceptions.GroupNotFoundException.getInstance(null, groupid);
		}
		return group;
	}

	@Override
	public Group getGroupByName(String groupName, String domainid) {
		try {
			Group group = groupDao.findByName(groupName, domainid);
			return group;
		} catch (NoResultException e) {
			throw Exceptions.GroupNotFoundException.getInstance(null, groupName);
		}
	}

	// TODO ignore @sql.handle_conflicts(conflict_type='group')
	@Override
	public Group updateGroup(String groupid, Group group) {
		Group oldGroup = groupDao.findById(groupid);
		if (group.isDescriptionUpdated()) {
			oldGroup.setDescription(group.getDescription());
		}
		if (group.isDomainUpdated()) {
			oldGroup.setDomain(group.getDomain());
		}
		if (group.isExtraUpdated()) {
			oldGroup.setExtra(group.getExtra());
		}
		if (group.isNameUpdated()) {
			oldGroup.setName(group.getName());
		}
		return groupDao.merge(oldGroup);
	}

	@Override
	public void deleteGroup(String groupid) {
		Group group = getGroup(groupid);
		List<UserGroupMembership> memberships = userGroupMembershipDao.listByGroup(groupid);
		for (UserGroupMembership userGroupMembership : memberships) {
			userGroupMembershipDao.remove(userGroupMembership);
		}
		groupDao.remove(group);
	}

	@Override
	public boolean isDomainAware() {
		return true;
	}

	@Override
	public Integer getListLimit() {
		return Config.getOpt(Config.Type.identity, "list_limit").asInteger();
	}

	@Override
	public boolean generateUuids() {
		return true;
	}

	@Override
	public void setAssignmentApi(AssignmentApi assignmentApi) {

	}

	@Override
	public boolean isMultipleDomainsSupported() {
		return this.isDomainAware()
				|| Config.getOpt(Config.Type.identity, "domain_specific_drivers_enabled").asBoolean();
	}

}
