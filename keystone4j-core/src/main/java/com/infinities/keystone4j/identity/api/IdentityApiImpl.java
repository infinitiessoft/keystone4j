package com.infinities.keystone4j.identity.api;

import java.util.List;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.command.group.CreateGroupCommand;
import com.infinities.keystone4j.identity.command.group.DeleteGroupCommand;
import com.infinities.keystone4j.identity.command.group.GetGroupCommand;
import com.infinities.keystone4j.identity.command.group.ListGroupsCommand;
import com.infinities.keystone4j.identity.command.group.ListGroupsForUserCommand;
import com.infinities.keystone4j.identity.command.group.UpdateGroupCommand;
import com.infinities.keystone4j.identity.command.user.AddUserToGroupCommand;
import com.infinities.keystone4j.identity.command.user.AuthenticateCommand;
import com.infinities.keystone4j.identity.command.user.CheckUserInGroupCommand;
import com.infinities.keystone4j.identity.command.user.CreateUserCommand;
import com.infinities.keystone4j.identity.command.user.DeleteUserCommand;
import com.infinities.keystone4j.identity.command.user.GetUserByNameCommand;
import com.infinities.keystone4j.identity.command.user.GetUserCommand;
import com.infinities.keystone4j.identity.command.user.ListUsersCommand;
import com.infinities.keystone4j.identity.command.user.ListUsersInGroupCommand;
import com.infinities.keystone4j.identity.command.user.RemoveUserFromGroupCommand;
import com.infinities.keystone4j.identity.command.user.UpdateUserCommand;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.token.TokenApi;

public class IdentityApiImpl implements IdentityApi {

	private final CredentialApi credentialApi;
	private final TokenApi tokenApi;
	private final IdentityDriver identityDriver;


	public IdentityApiImpl(CredentialApi credentialApi, TokenApi tokenApi, IdentityDriver identityDriver) {
		super();
		this.credentialApi = credentialApi;
		this.tokenApi = tokenApi;
		this.identityDriver = identityDriver;
	}

	@Override
	public User getUser(String userid, String domainid) {
		GetUserCommand command = new GetUserCommand(credentialApi, tokenApi, this, identityDriver, userid, domainid);
		return command.execute();
	}

	@Override
	public Group getGroup(String groupid, String domainid) {
		GetGroupCommand command = new GetGroupCommand(credentialApi, tokenApi, this, identityDriver, groupid, domainid);
		return command.execute();
	}

	@Override
	public Group createGroup(Group group) {
		CreateGroupCommand command = new CreateGroupCommand(credentialApi, tokenApi, this, identityDriver, group);
		return command.execute();
	}

	@Override
	public List<Group> listGroups(String domainid) {
		ListGroupsCommand command = new ListGroupsCommand(credentialApi, tokenApi, this, identityDriver, domainid);
		return command.execute();
	}

	@Override
	public Group updateGroup(String groupid, Group group, String domainid) {
		UpdateGroupCommand command = new UpdateGroupCommand(credentialApi, tokenApi, this, identityDriver, groupid, group,
				domainid);
		return command.execute();
	}

	@Override
	public Group deleteGroup(String groupid, String domainid) {
		DeleteGroupCommand command = new DeleteGroupCommand(credentialApi, tokenApi, this, identityDriver, groupid, domainid);
		return command.execute();
	}

	@Override
	public List<Group> listGroupsForUser(String userid, String domainid) {
		ListGroupsForUserCommand command = new ListGroupsForUserCommand(credentialApi, tokenApi, this, identityDriver,
				userid, domainid);
		return command.execute();
	}

	@Override
	public User createUser(User user) {
		CreateUserCommand command = new CreateUserCommand(credentialApi, tokenApi, this, identityDriver, user);
		return command.execute();
	}

	@Override
	public List<User> listUsers(String domainid) {
		ListUsersCommand command = new ListUsersCommand(credentialApi, tokenApi, this, identityDriver, domainid);
		return command.execute();
	}

	@Override
	public User updateUser(String userid, User user, String domainid) {
		UpdateUserCommand command = new UpdateUserCommand(credentialApi, tokenApi, this, identityDriver, userid, user,
				domainid);
		return command.execute();
	}

	@Override
	public User deleteUser(String userid, String domainid) {
		DeleteUserCommand command = new DeleteUserCommand(credentialApi, tokenApi, this, identityDriver, userid, domainid);
		return command.execute();
	}

	@Override
	public List<User> listUsersInGroup(String groupid, String domainid) {
		ListUsersInGroupCommand command = new ListUsersInGroupCommand(credentialApi, tokenApi, this, identityDriver,
				groupid, domainid);
		return command.execute();
	}

	@Override
	public User removeUserFromGroup(String userid, String groupid, String domainid) {
		RemoveUserFromGroupCommand command = new RemoveUserFromGroupCommand(credentialApi, tokenApi, this, identityDriver,
				userid, groupid, domainid);
		return command.execute();
	}

	@Override
	public User checkUserInGroup(String userid, String groupid, String domainid) {
		CheckUserInGroupCommand command = new CheckUserInGroupCommand(credentialApi, tokenApi, this, identityDriver, userid,
				groupid, domainid);
		return command.execute();
	}

	@Override
	public User addUserToGroup(String userid, String groupid, String domainid) {
		AddUserToGroupCommand command = new AddUserToGroupCommand(credentialApi, tokenApi, this, identityDriver, userid,
				groupid, domainid);
		return command.execute();
	}

	@Override
	public User authenticate(String userid, String password, String domainid) {
		AuthenticateCommand command = new AuthenticateCommand(credentialApi, tokenApi, this, identityDriver, userid,
				password, domainid);
		return command.execute();
	}

	@Override
	public User getUserByName(String userName, String domainid) {
		GetUserByNameCommand command = new GetUserByNameCommand(credentialApi, tokenApi, this, identityDriver, userName,
				domainid);
		return command.execute();
	}

}
