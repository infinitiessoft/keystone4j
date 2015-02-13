package com.infinities.keystone4j.identity.api;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.api.command.decorator.ResponseTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.identity.api.command.group.CreateGroupCommand;
import com.infinities.keystone4j.identity.api.command.group.DeleteGroupCommand;
import com.infinities.keystone4j.identity.api.command.group.GetGroupByNameCommand;
import com.infinities.keystone4j.identity.api.command.group.GetGroupCommand;
import com.infinities.keystone4j.identity.api.command.group.ListGroupsCommand;
import com.infinities.keystone4j.identity.api.command.group.ListGroupsForUserCommand;
import com.infinities.keystone4j.identity.api.command.group.UpdateGroupCommand;
import com.infinities.keystone4j.identity.api.command.user.AddUserToGroupCommand;
import com.infinities.keystone4j.identity.api.command.user.AuthenticateCommand;
import com.infinities.keystone4j.identity.api.command.user.ChangePasswordCommand;
import com.infinities.keystone4j.identity.api.command.user.CheckUserInGroupCommand;
import com.infinities.keystone4j.identity.api.command.user.CreateUserCommand;
import com.infinities.keystone4j.identity.api.command.user.DeleteUserCommand;
import com.infinities.keystone4j.identity.api.command.user.EmitInvalidateUserTokenPersistenceCommand;
import com.infinities.keystone4j.identity.api.command.user.GetUserByNameCommand;
import com.infinities.keystone4j.identity.api.command.user.GetUserCommand;
import com.infinities.keystone4j.identity.api.command.user.ListUsersCommand;
import com.infinities.keystone4j.identity.api.command.user.ListUsersInGroupCommand;
import com.infinities.keystone4j.identity.api.command.user.RemoveUserFromGroupCommand;
import com.infinities.keystone4j.identity.api.command.user.UpdateUserCommand;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.notification.Notifications;

public class IdentityApiImpl implements IdentityApi {

	// private final AssignmentApi assignmentApi;
	private final CredentialApi credentialApi;
	private final RevokeApi revokeApi;
	// private final TokenApi tokenApi;
	private final IdentityDriver identityDriver;
	private final IdMappingApi idMappingApi;
	private final static String _USER = "user";
	// private final static String _USER_PASSWORD = "user_password";
	// private final static String _USER_REMOVED_FROM_GROUP =
	// "user_removed_from_group";
	private final static String _GROUP = "group";
	private AssignmentApi assignmentApi;


	public IdentityApiImpl(CredentialApi credentialApi, RevokeApi revokeApi, IdMappingApi idMappingApi,
			IdentityDriver identityDriver) {
		super();
		// this.assignmentApi = assignmentApi;
		this.credentialApi = credentialApi;
		this.revokeApi = revokeApi;
		this.idMappingApi = idMappingApi;
		this.identityDriver = identityDriver;
	}

	// TODO ignore @exception_translated('user')
	@Override
	public User getUser(String userid) throws Exception {
		GetUserCommand command = new GetUserCommand(getAssignmentApi(), credentialApi, revokeApi, this, idMappingApi,
				identityDriver, userid);
		domainsConfigured(command);
		return command.execute();
	}

	// TODO ignore @exception_translated('group')
	@Override
	public Group getGroup(String groupid) throws Exception {
		GetGroupCommand command = new GetGroupCommand(getAssignmentApi(), credentialApi, revokeApi, this, idMappingApi,
				identityDriver, groupid);
		domainsConfigured(command);
		return command.execute();
	}

	// TODO ignore @exception_translated('group')
	@Override
	public Group createGroup(Group group) throws Exception {
		CreateGroupCommand command = new CreateGroupCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, group);
		domainsConfigured(command);
		return Notifications.created(command, _GROUP, true, 1, "id").execute();
	}

	// TODO ignore @exception_translated('group')
	@Override
	public List<Group> listGroups(String domainid, Hints hints) throws Exception {
		ListGroupsCommand listGroupsCommand = new ListGroupsCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, domainid);
		TruncatedCommand<Group> command = new ResponseTruncatedCommand<Group>(listGroupsCommand, identityDriver);
		domainsConfigured(listGroupsCommand);
		return command.execute(hints);
	}

	// TODO ignore @exception_translated('group')
	@Override
	public Group updateGroup(String groupid, Group group) throws Exception {
		UpdateGroupCommand command = new UpdateGroupCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, groupid, group);
		domainsConfigured(command);
		return Notifications.updated(command, _GROUP).execute();
	}

	@Override
	public Group deleteGroup(String groupid) throws Exception {
		DeleteGroupCommand command = new DeleteGroupCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, groupid);
		domainsConfigured(command);
		return Notifications.deleted(command, _GROUP).execute();
	}

	// TODO ignore @exception_translated('group')
	@Override
	public List<Group> listGroupsForUser(String userid, Hints hints) throws Exception {
		ListGroupsForUserCommand listGroupsForUsercommand = new ListGroupsForUserCommand(getAssignmentApi(), credentialApi,
				revokeApi, this, idMappingApi, identityDriver, userid);
		TruncatedCommand<Group> command = new ResponseTruncatedCommand<Group>(listGroupsForUsercommand, identityDriver);
		domainsConfigured(listGroupsForUsercommand);
		return command.execute(hints);
	}

	// TODO ignore @exception_translated('user')
	@Override
	public User createUser(User user) throws Exception {
		CreateUserCommand command = new CreateUserCommand(getAssignmentApi(), credentialApi, revokeApi, this, idMappingApi,
				identityDriver, user);
		domainsConfigured(command);
		return Notifications.created(command, _USER, true, 1, "id").execute();
	}

	// TODO ignore @exception_translated('user')
	@Override
	public List<User> listUsers(String domainid, Hints hints) throws Exception {
		ListUsersCommand listUserCommand = new ListUsersCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, domainid);
		TruncatedCommand<User> command = new ResponseTruncatedCommand<User>(listUserCommand, identityDriver);
		domainsConfigured(listUserCommand);
		return command.execute(hints);
	}

	// TODO ignore @exception_translated('user')
	@Override
	public User updateUser(String userid, User userRef) throws Exception {
		UpdateUserCommand command = new UpdateUserCommand(getAssignmentApi(), credentialApi, revokeApi, this, idMappingApi,
				identityDriver, userid, userRef);
		domainsConfigured(command);
		return Notifications.updated(command, _USER).execute();
	}

	// TODO ignore @exception_translated('user')
	@Override
	public User deleteUser(String userid) throws Exception {
		DeleteUserCommand command = new DeleteUserCommand(getAssignmentApi(), credentialApi, revokeApi, this, idMappingApi,
				identityDriver, userid, userid);
		domainsConfigured(command);
		return Notifications.deleted(command, _USER).execute();
	}

	// TODO ignore @exception_translated('group')
	@Override
	public List<User> listUsersInGroup(String groupid, Hints hints) throws Exception {
		ListUsersInGroupCommand listUsersInGroupCommand = new ListUsersInGroupCommand(getAssignmentApi(), credentialApi,
				revokeApi, this, idMappingApi, identityDriver, groupid);
		TruncatedCommand<User> command = new ResponseTruncatedCommand<User>(listUsersInGroupCommand, identityDriver);
		domainsConfigured(listUsersInGroupCommand);
		return command.execute(hints);
	}

	// TODO @exception_translated('group')
	@Override
	public User removeUserFromGroup(String userid, String groupid) throws Exception {
		RemoveUserFromGroupCommand command = new RemoveUserFromGroupCommand(getAssignmentApi(), credentialApi, revokeApi,
				this, idMappingApi, identityDriver, userid, groupid);
		domainsConfigured(command);
		return command.execute();
	}

	// TODO @exception_translated('group')
	@Override
	public User checkUserInGroup(String userid, String groupid) throws Exception {
		CheckUserInGroupCommand command = new CheckUserInGroupCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, userid, groupid);
		domainsConfigured(command);
		return command.execute();
	}

	// TODO @exception_translated('group')
	@Override
	public User addUserToGroup(String userid, String groupid) throws Exception {
		AddUserToGroupCommand command = new AddUserToGroupCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, userid, groupid);
		domainsConfigured(command);
		return command.execute();
	}

	// TODO ignore @notifications.emit_event('authenticate')
	// TODO ignore @exception_translated('assertion')
	@Override
	public User authenticate(String userid, String password) throws Exception {
		AuthenticateCommand command = new AuthenticateCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, userid, password);
		domainsConfigured(command);
		return command.execute();
	}

	// TODO ignore @exception_translated('user')
	@Override
	public User getUserByName(String userName, String domainid) throws Exception {
		GetUserByNameCommand command = new GetUserByNameCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, userName, domainid);
		domainsConfigured(command);
		return command.execute();
	}

	private void domainsConfigured(AbstractIdentityCommand command) throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		if (!command.getDomainConfigs().isConfigured()
				&& Config.Instance.getOpt(Config.Type.identity, "domain_specific_drivers_enabled").asBoolean()) {
			command.getDomainConfigs().setupDomainDrivers(command.getIdentityDriver(), command.getAssignmentApi());
		}
	}

	// TODO @exception_translated('group')
	@Override
	public Group getGroupByName(String groupName, String domainid) throws Exception {
		GetGroupByNameCommand command = new GetGroupByNameCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, groupName, domainid);
		domainsConfigured(command);
		return command.execute();
	}

	@Override
	public void changePassword(KeystoneContext context, String userid, String originalPassword, String newPassword)
			throws Exception {
		ChangePasswordCommand command = new ChangePasswordCommand(getAssignmentApi(), credentialApi, revokeApi, this,
				idMappingApi, identityDriver, userid, originalPassword, newPassword);
		domainsConfigured(command);
		command.execute();
	}

	@Override
	public void emitInvalidateUserTokenPersistence(String userid) throws Exception {
		NonTruncatedCommand<User> command = Notifications.internal(new EmitInvalidateUserTokenPersistenceCommand(
				getAssignmentApi(), credentialApi, revokeApi, this, idMappingApi, identityDriver, userid),
				Notifications.INVALIDATE_USER_TOKEN_PERSISTENCE);
		command.execute();
	}

	@Override
	public boolean isMultipleDomainsSupported() throws Exception {
		return identityDriver.isMultipleDomainsSupported();
	}

	@Override
	public void assertUserEnabled(String id, User user) throws Exception {
		if (user == null) {
			user = getUser(id);
		}

		getAssignmentApi().assertDomainEnabled(user.getDomainId(), null);

		if (!user.getEnabled()) {
			String msg = String.format("User is disabled: %s", id);
			throw new IllegalStateException(msg);
		}
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	@Override
	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

}
