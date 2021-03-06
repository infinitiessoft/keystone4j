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
package com.infinities.keystone4j.identity.controller.impl;

import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.controller.action.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.controller.action.decorator.ProtectedDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.identity.controller.action.group.CreateGroupAction;
import com.infinities.keystone4j.identity.controller.action.group.DeleteGroupAction;
import com.infinities.keystone4j.identity.controller.action.group.GetGroupAction;
import com.infinities.keystone4j.identity.controller.action.group.ListGroupsAction;
import com.infinities.keystone4j.identity.controller.action.group.ListGroupsForUserAction;
import com.infinities.keystone4j.identity.controller.action.group.UpdateGroupAction;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.identity.controllers.GroupV3 20141211

public class GroupV3ControllerImpl extends BaseController implements GroupV3Controller {

	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public GroupV3ControllerImpl(IdentityApi identityApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public MemberWrapper<Group> createGroup(Group group) throws Exception {
		ProtectedAction<Group> command = new ProtectedDecorator<Group>(new CreateGroupAction(identityApi, tokenProviderApi,
				policyApi, group), tokenProviderApi, policyApi);
		MemberWrapper<Group> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Group> listGroups() throws Exception {
		FilterProtectedAction<Group> command = new FilterProtectedDecorator<Group>(new ListGroupsAction(identityApi,
				tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		CollectionWrapper<Group> ret = command.execute(getRequest(), "domain_id", "name");
		return ret;
	}

	@Override
	public MemberWrapper<Group> getGroup(String groupid) throws Exception {
		Group ref = getMemberFromDriver(groupid);
		ProtectedAction<Group> command = new ProtectedDecorator<Group>(new GetGroupAction(identityApi, tokenProviderApi,
				policyApi, groupid), tokenProviderApi, policyApi, ref, null);
		MemberWrapper<Group> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public MemberWrapper<Group> updateGroup(String groupid, Group group) throws Exception {
		Group ref = getMemberFromDriver(groupid);
		ProtectedAction<Group> command = new ProtectedDecorator<Group>(new UpdateGroupAction(identityApi, tokenProviderApi,
				policyApi, groupid, group), tokenProviderApi, policyApi, ref, group);
		MemberWrapper<Group> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteGroup(String groupid) throws Exception {
		Group ref = getMemberFromDriver(groupid);
		ProtectedAction<Group> command = new ProtectedDecorator<Group>(new DeleteGroupAction(identityApi, tokenProviderApi,
				policyApi, groupid), tokenProviderApi, policyApi, ref, null);
		command.execute(getRequest());
	}

	@Override
	public CollectionWrapper<Group> listGroupsForUser(String userid) throws Exception {
		Entry<String, String> entrys = Maps.immutableEntry("user_id", userid);
		FilterProtectedAction<Group> command = new FilterProtectedDecorator<Group>(new ListGroupsForUserAction(identityApi,
				tokenProviderApi, policyApi, userid), tokenProviderApi, policyApi, entrys);
		CollectionWrapper<Group> ret = command.execute(getRequest(), "name");
		return ret;
	}

	public Group getMemberFromDriver(String groupid) throws Exception {
		return identityApi.getGroup(groupid);
	}

}
