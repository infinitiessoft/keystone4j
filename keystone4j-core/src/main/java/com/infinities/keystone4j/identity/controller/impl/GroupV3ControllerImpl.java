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
import com.infinities.keystone4j.identity.action.group.CreateGroupAction;
import com.infinities.keystone4j.identity.action.group.DeleteGroupAction;
import com.infinities.keystone4j.identity.action.group.GetGroupAction;
import com.infinities.keystone4j.identity.action.group.ListGroupsAction;
import com.infinities.keystone4j.identity.action.group.ListGroupsForUserAction;
import com.infinities.keystone4j.identity.action.group.UpdateGroupAction;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.GroupWrapper;
import com.infinities.keystone4j.model.identity.GroupsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class GroupV3ControllerImpl extends BaseController implements GroupV3Controller {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public GroupV3ControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public GroupWrapper createGroup(Group group) {
		parMap.put("group", group);
		Action<Group> command = new PolicyCheckDecorator<Group>(new CreateGroupAction(assignmentApi, tokenApi, identityApi,
				group), null, tokenApi, policyApi, parMap);
		Group ret = command.execute(getRequest());
		return new GroupWrapper(ret, getRequest());
	}

	@Override
	public GroupsWrapper listGroups(String domainid, String name, int page, int perPage) {
		parMap.put("domainid", domainid);
		parMap.put("name", name);
		Action<List<Group>> command = new FilterCheckDecorator<List<Group>>(new PaginateDecorator<Group>(
				new ListGroupsAction(assignmentApi, tokenApi, identityApi, domainid, name), page, perPage), tokenApi,
				policyApi, parMap);

		List<Group> ret = command.execute(getRequest());
		return new GroupsWrapper(ret, getRequest());
	}

	@Override
	public GroupWrapper getGroup(String groupid) {
		parMap.put("groupid", groupid);
		Action<Group> command = new PolicyCheckDecorator<Group>(new GetGroupAction(assignmentApi, tokenApi, identityApi,
				groupid), null, tokenApi, policyApi, parMap);
		Group ret = command.execute(getRequest());
		return new GroupWrapper(ret, getRequest());
	}

	@Override
	public GroupWrapper updateGroup(String groupid, Group group) {
		parMap.put("groupid", groupid);
		parMap.put("group", group);
		Action<Group> command = new PolicyCheckDecorator<Group>(new UpdateGroupAction(assignmentApi, tokenApi, identityApi,
				groupid, group), null, tokenApi, policyApi, parMap);
		Group ret = command.execute(getRequest());
		return new GroupWrapper(ret, getRequest());
	}

	@Override
	public void deleteGroup(String groupid) {
		parMap.put("groupid", groupid);
		Action<Group> command = new PolicyCheckDecorator<Group>(new DeleteGroupAction(assignmentApi, tokenApi, identityApi,
				groupid), null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public GroupsWrapper listGroupsForUser(String userid, String name, int page, int perPage) {
		parMap.put("userid", userid);
		parMap.put("name", name);
		Action<List<Group>> command = new FilterCheckDecorator<List<Group>>(new PaginateDecorator<Group>(
				new ListGroupsForUserAction(assignmentApi, tokenApi, identityApi, userid, name), page, perPage), tokenApi,
				policyApi, parMap);

		List<Group> ret = command.execute(getRequest());
		return new GroupsWrapper(ret, getRequest());
	}

}
