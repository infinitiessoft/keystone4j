package com.infinities.keystone4j.identity.driver;

import java.util.List;

import javax.persistence.NoResultException;

import com.infinities.keystone4j.PasswordUtils;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.model.Group;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.identity.model.UserGroupMembership;
import com.infinities.keystone4j.jpa.impl.GroupDao;
import com.infinities.keystone4j.jpa.impl.UserDao;
import com.infinities.keystone4j.jpa.impl.UserGroupMembershipDao;

public class IdentityJpaDriver implements IdentityDriver {

	private final UserDao userDao;
	private final GroupDao groupDao;
	private final UserGroupMembershipDao userGroupMembershipDao;
	private final static String USER_NOT_FOUND = "User not found in group";
	private final static String INVALID_USER_PASSWORD = "Invalid user / password";


	public IdentityJpaDriver() {
		super();
		this.userDao = new UserDao();
		this.groupDao = new GroupDao();
		this.userGroupMembershipDao = new UserGroupMembershipDao();
	}

	@Override
	public User authenticate(String userid, String password) {
		User user = null;
		try {
			user = getUser(userid);
		} catch (Exception e) {
			// replace AssertionError
			throw new IllegalArgumentException(INVALID_USER_PASSWORD);
		}

		boolean checked = PasswordUtils.checkPassword(password, user.getPassword());
		if (!checked) {
			// replace AssertionError
			throw new IllegalArgumentException(INVALID_USER_PASSWORD);
		}
		return user;
	}

	@Override
	public User createUser(User user) {
		user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
		userDao.persist(user);
		return user;
	}

	@Override
	public List<User> listUsers() {
		return userDao.findAll();
	}

	@Override
	public List<User> listUsersInGroup(String groupid) {
		getGroup(groupid);
		List<User> users = userGroupMembershipDao.listUserByGroup(groupid);
		// List<User> users = Lists.newArrayList();
		// for (UserGroupMembership membership : memberships) {
		// users.add(membership.getUser());
		// }
		return users;
	}

	@Override
	public User getUser(String userid) {
		User user = userDao.findById(userid);
		if (user == null) {
			throw Exceptions.UserNotFoundException.getInstance(null, userid);
		}
		return user;
	}

	@Override
	public User updateUser(String userid, User user) {
		User oldUser = getUser(userid);
		if (user.isDefaultProjectUpdated()) {
			oldUser.setDefault_project(user.getDefault_project());
		}
		if (user.isDescriptionUpdated()) {
			oldUser.setDescription(user.getDescription());
		}
		if (user.isDomainUpdated()) {
			oldUser.setDomain(user.getDomain());
		}
		if (user.isEmailUpdated()) {
			oldUser.setEmail(user.getEmail());
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
			user.setPassword(PasswordUtils.hashPassword(user.getPassword()));
			oldUser.setPassword(user.getPassword());
		}

		return userDao.merge(oldUser);
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
			throw Exceptions.NotFoundException.getInstance(USER_NOT_FOUND);
		}
	}

	@Override
	public void removeUserFromGroup(String userid, String groupid) {
		try {
			UserGroupMembership membership = userGroupMembershipDao.findByUserGroup(userid, groupid);
			userGroupMembershipDao.remove(membership);
		} catch (NoResultException e) {
			throw Exceptions.NotFoundException.getInstance(USER_NOT_FOUND);
		}
	}

	@Override
	public void deleteUser(String userid) {
		User user = getUser(userid);
		userDao.remove(user);
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
	public Group createGroup(Group group) {
		groupDao.persist(group);
		return group;
	}

	@Override
	public List<Group> listGroups() {
		return groupDao.findAll();
	}

	@Override
	public List<Group> listGroupsForUser(String userid) {
		getUser(userid);
		List<Group> groups = userGroupMembershipDao.listGroupsByUser(userid);
		// List<Group> groups = Lists.newArrayList();
		// for (UserGroupMembership membership : memberships) {
		// groups.add(membership.getGroup());
		// }
		return groups;
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
	public boolean isDomainAware() {
		return true;
	}

	@Override
	public void deleteGroup(String groupid) {
		Group group = getGroup(groupid);
		groupDao.remove(group);
	}

}
