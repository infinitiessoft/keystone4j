package com.infinities.keystone4j.identity.controller.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.BaseController;
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
import com.infinities.keystone4j.identity.callback.CheckUserAndGroupProtection;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.model.User;
import com.infinities.keystone4j.identity.model.UserParam;
import com.infinities.keystone4j.identity.model.UserWrapper;
import com.infinities.keystone4j.identity.model.UsersWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class UserV3ControllerImpl extends BaseController implements UserV3Controller {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public UserV3ControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public UserWrapper createUser(User user) {
		parMap.put("user", user);
		Action<User> command = new PolicyCheckDecorator<User>(new CreateUserAction(assignmentApi, tokenApi, identityApi,
				user), null, tokenApi, policyApi, parMap);
		User ret = command.execute(getRequest());
		return new UserWrapper(ret, getRequest());
	}

	@Override
	public UsersWrapper listUsers(String domainid, String email, Boolean enabled, String name, int page, int perPage) {
		parMap.put("domainid", domainid);
		parMap.put("email", email);
		parMap.put("enabled", enabled);
		parMap.put("name", name);
		Action<List<User>> command = new FilterCheckDecorator<List<User>>(new PaginateDecorator<User>(new ListUsersAction(
				assignmentApi, tokenApi, identityApi, domainid, email, enabled, name), page, perPage), tokenApi, policyApi,
				parMap);

		List<User> ret = command.execute(getRequest());
		return new UsersWrapper(ret, getRequest());
	}

	@Override
	public UserWrapper getUser(String userid) {
		parMap.put("userid", userid);
		Action<User> command = new PolicyCheckDecorator<User>(
				new GetUserAction(assignmentApi, tokenApi, identityApi, userid), null, tokenApi, policyApi, parMap);
		User ret = command.execute(getRequest());
		return new UserWrapper(ret, getRequest());
	}

	@Override
	public UserWrapper updateUser(String userid, User user) {
		parMap.put("userid", userid);
		parMap.put("user", user);
		Action<User> command = new PolicyCheckDecorator<User>(new UpdateUserAction(assignmentApi, tokenApi, identityApi,
				userid, user), null, tokenApi, policyApi, parMap);
		User ret = command.execute(getRequest());
		return new UserWrapper(ret, getRequest());
	}

	@Override
	public void deleteUser(String userid) {
		parMap.put("userid", userid);
		Action<User> command = new PolicyCheckDecorator<User>(new DeleteUserAction(assignmentApi, tokenApi, identityApi,
				userid), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public UsersWrapper listUsersInGroup(String groupid, String domainid, String email, Boolean enabled, String name,
			int page, int perPage) {
		parMap.put("groupid", groupid);
		parMap.put("domainid", domainid);
		parMap.put("email", email);
		parMap.put("enabled", enabled);

		Action<List<User>> command = new FilterCheckDecorator<List<User>>(new PaginateDecorator<User>(
				new ListUsersInGroupAction(assignmentApi, tokenApi, identityApi, groupid, domainid, email, enabled, name),
				page, perPage), tokenApi, policyApi, parMap);

		List<User> ret = command.execute(getRequest());
		return new UsersWrapper(ret, getRequest());
	}

	@Override
	public void addUserToGroup(String groupid, String userid) {
		parMap.put("groupid", groupid);
		parMap.put("userid", userid);
		CheckUserAndGroupProtection callback = new CheckUserAndGroupProtection(userid, groupid, identityApi, tokenApi,
				policyApi);
		Action<User> command = new PolicyCheckDecorator<User>(new AddUserToGroupAction(assignmentApi, tokenApi, identityApi,
				userid, groupid), callback, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public void checkUserInGroup(String groupid, String userid) {
		parMap.put("groupid", groupid);
		parMap.put("userid", userid);
		CheckUserAndGroupProtection callback = new CheckUserAndGroupProtection(userid, groupid, identityApi, tokenApi,
				policyApi);
		Action<User> command = new PolicyCheckDecorator<User>(new CheckUserInGroupAction(assignmentApi, tokenApi,
				identityApi, userid, groupid), callback, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public void removeUserFromGroup(String groupid, String userid) {
		parMap.put("groupid", groupid);
		parMap.put("userid", userid);
		CheckUserAndGroupProtection callback = new CheckUserAndGroupProtection(userid, groupid, identityApi, tokenApi,
				policyApi);
		Action<User> command = new PolicyCheckDecorator<User>(new RemoveUserFromGroupAction(assignmentApi, tokenApi,
				identityApi, userid, groupid), callback, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public void changePassword(String userid, UserParam user) {
		parMap.put("user", user);
		parMap.put("userid", userid);
		Action<User> command = new PolicyCheckDecorator<User>(new ChangePasswordAction(assignmentApi, tokenApi, identityApi,
				userid, user), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

}
