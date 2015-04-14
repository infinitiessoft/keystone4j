package com.infinities.keystone4j.identity.controller.impl;

import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.controller.action.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.controller.action.decorator.ProtectedDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.controller.UserV3Controller;
import com.infinities.keystone4j.identity.controller.action.user.AddUserToGroupAction;
import com.infinities.keystone4j.identity.controller.action.user.ChangePasswordAction;
import com.infinities.keystone4j.identity.controller.action.user.CheckUserInGroupAction;
import com.infinities.keystone4j.identity.controller.action.user.CreateUserAction;
import com.infinities.keystone4j.identity.controller.action.user.DeleteUserAction;
import com.infinities.keystone4j.identity.controller.action.user.GetUserAction;
import com.infinities.keystone4j.identity.controller.action.user.ListUsersAction;
import com.infinities.keystone4j.identity.controller.action.user.ListUsersInGroupAction;
import com.infinities.keystone4j.identity.controller.action.user.RemoveUserFromGroupAction;
import com.infinities.keystone4j.identity.controller.action.user.UpdateUserAction;
import com.infinities.keystone4j.identity.controller.callback.CheckUserAndGroupProtection;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.UserParam;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.identity.controllers.UserV3 20141211

public class UserV3ControllerImpl extends BaseController implements UserV3Controller {

	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public UserV3ControllerImpl(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public MemberWrapper<User> createUser(User user) throws Exception {
		ProtectedAction<User> command = new ProtectedDecorator<User>(new CreateUserAction(identityApi, tokenProviderApi,
				policyApi, user), tokenProviderApi, policyApi);
		MemberWrapper<User> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<User> listUsers() throws Exception {
		FilterProtectedAction<User> command = new FilterProtectedDecorator<User>(new ListUsersAction(identityApi,
				tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		CollectionWrapper<User> ret = command.execute(getRequest(), "domain_id", "enabled", "name");
		return ret;
	}

	@Override
	public MemberWrapper<User> getUser(String userid) throws Exception {
		User ref = getMemberFromDriver(userid);
		ProtectedAction<User> command = new ProtectedDecorator<User>(new GetUserAction(identityApi, tokenProviderApi,
				policyApi, userid), tokenProviderApi, policyApi, ref, null);
		MemberWrapper<User> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public MemberWrapper<User> updateUser(String userid, User user) throws Exception {
		User ref = getMemberFromDriver(userid);
		ProtectedAction<User> command = new ProtectedDecorator<User>(new UpdateUserAction(identityApi, tokenProviderApi,
				policyApi, userid, user), tokenProviderApi, policyApi, ref, user);
		MemberWrapper<User> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteUser(String userid) throws Exception {
		User ref = getMemberFromDriver(userid);
		ProtectedAction<User> command = new ProtectedDecorator<User>(new DeleteUserAction(identityApi, tokenProviderApi,
				policyApi, userid), tokenProviderApi, policyApi, ref, null);
		command.execute(getRequest());
	}

	@Override
	public CollectionWrapper<User> listUsersInGroup(String groupid) throws Exception {
		Entry<String, String> entrys = Maps.immutableEntry("group_id", groupid);
		FilterProtectedAction<User> command = new FilterProtectedDecorator<User>(new ListUsersInGroupAction(identityApi,
				tokenProviderApi, policyApi, groupid), tokenProviderApi, policyApi, entrys);
		CollectionWrapper<User> ret = command.execute(getRequest(), "domain_id", "enabled", "name");
		return ret;
	}

	@Override
	public void addUserToGroup(String groupid, String userid) throws Exception {
		CheckUserAndGroupProtection callback = new CheckUserAndGroupProtection(userid, groupid, identityApi,
				tokenProviderApi, policyApi);
		ProtectedAction<User> command = new ProtectedDecorator<User>(new AddUserToGroupAction(identityApi, tokenProviderApi,
				policyApi, userid, groupid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void checkUserInGroup(String groupid, String userid) throws Exception {
		CheckUserAndGroupProtection callback = new CheckUserAndGroupProtection(userid, groupid, identityApi,
				tokenProviderApi, policyApi);
		ProtectedAction<User> command = new ProtectedDecorator<User>(new CheckUserInGroupAction(identityApi,
				tokenProviderApi, policyApi, userid, groupid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void removeUserFromGroup(String groupid, String userid) throws Exception {
		CheckUserAndGroupProtection callback = new CheckUserAndGroupProtection(userid, groupid, identityApi,
				tokenProviderApi, policyApi);
		ProtectedAction<User> command = new ProtectedDecorator<User>(new RemoveUserFromGroupAction(identityApi,
				tokenProviderApi, policyApi, userid, groupid), callback, tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public void changePassword(String userid, UserParam user) throws Exception {
		User ref = getMemberFromDriver(userid);
		ProtectedAction<User> command = new ProtectedDecorator<User>(new ChangePasswordAction(identityApi, tokenProviderApi,
				policyApi, userid, user), tokenProviderApi, policyApi, ref, user);
		command.execute(getRequest());
	}

	public User getMemberFromDriver(String userid) throws Exception {
		return identityApi.getUser(userid);
	}
}
