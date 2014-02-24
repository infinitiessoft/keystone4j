package com.infinities.keystone4j.identity.controller.impl;

import java.util.List;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.action.user.AddUserToGroupAction;
import com.infinities.keystone4j.identity.action.user.ChangePasswordAction;
import com.infinities.keystone4j.identity.action.user.CheckUserInGroupAction;
import com.infinities.keystone4j.identity.action.user.CreateUserAction;
import com.infinities.keystone4j.identity.action.user.DeleteUserAction;
import com.infinities.keystone4j.identity.action.user.GetUserAction;
import com.infinities.keystone4j.identity.action.user.ListUsersAction;
import com.infinities.keystone4j.identity.action.user.ListUsersInGroupAction;
import com.infinities.keystone4j.identity.action.user.RemoveUserFromGroupAction;
import com.infinities.keystone4j.identity.action.user.UpdateUserAction;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.identity.model.UserParam;
import com.infinities.keystone4j.identity.model.UserWrapper;
import com.infinities.keystone4j.identity.model.UsersWrapper;

public class UserV3ControllerImpl implements UserV3Controller {

	private IdentityApi identityApi;


	public UserV3ControllerImpl(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	@Override
	public UserWrapper createUser(User user) {
		Action<User> command = new PolicyCheckDecorator<User>(new CreateUserAction(identityApi, user));
		User ret = command.execute();
		return new UserWrapper(ret);
	}

	@Override
	public UsersWrapper listUsers(String domainid, String email, Boolean enabled, String name, int page, int perPage) {
		Action<List<User>> command = new FilterCheckDecorator<List<User>>(new PaginateDecorator<User>(new ListUsersAction(
				identityApi, domainid, email, enabled, name), page, perPage));

		List<User> ret = command.execute();
		return new UsersWrapper(ret);
	}

	@Override
	public UserWrapper getUser(String userid) {
		Action<User> command = new PolicyCheckDecorator<User>(new GetUserAction(identityApi, userid));
		User ret = command.execute();
		return new UserWrapper(ret);
	}

	@Override
	public UserWrapper updateUser(String userid, User user) {
		Action<User> command = new PolicyCheckDecorator<User>(new UpdateUserAction(identityApi, userid, user));
		User ret = command.execute();
		return new UserWrapper(ret);
	}

	@Override
	public void deleteUser(String userid) {
		Action<User> command = new PolicyCheckDecorator<User>(new DeleteUserAction(identityApi, userid));
		command.execute();
	}

	@Override
	public UsersWrapper listUsersInGroup(String groupid, String domainid, String email, Boolean enabled, String name,
			int page, int perPage) {
		Action<List<User>> command = new FilterCheckDecorator<List<User>>(new PaginateDecorator<User>(
				new ListUsersInGroupAction(identityApi, groupid, domainid, email, enabled, name), page, perPage));

		List<User> ret = command.execute();
		return new UsersWrapper(ret);
	}

	@Override
	public void addUserToGroup(String groupid, String userid) {
		// TODO @controller.protected(callback=_check_user_and_group_protection)
		Action<User> command = new PolicyCheckDecorator<User>(new AddUserToGroupAction(identityApi, userid, groupid));
		command.execute();
	}

	@Override
	public void checkUserInGroup(String groupid, String userid) {
		// TODO @controller.protected(callback=_check_user_and_group_protection)
		Action<User> command = new PolicyCheckDecorator<User>(new CheckUserInGroupAction(identityApi, userid, groupid));
		command.execute();
	}

	@Override
	public void removeUserFromGroup(String groupid, String userid) {
		// TODO @controller.protected(callback=_check_user_and_group_protection)
		Action<User> command = new PolicyCheckDecorator<User>(new RemoveUserFromGroupAction(identityApi, userid, groupid));
		command.execute();
	}

	@Override
	public void changePassword(String userid, UserParam user) {
		Action<User> command = new PolicyCheckDecorator<User>(new ChangePasswordAction(identityApi, userid, user));
		command.execute();
	}

}
