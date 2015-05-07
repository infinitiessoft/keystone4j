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
package com.infinities.keystone4j.assignment.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.assignment.controller.action.grant.CheckGrantAction;
import com.infinities.keystone4j.assignment.controller.action.grant.CreateGrantAction;
import com.infinities.keystone4j.assignment.controller.action.grant.DeleteGrantAction;
import com.infinities.keystone4j.assignment.controller.action.grant.ListGrantsAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.CreateRoleAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.DeleteRoleAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.GetRoleAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.ListRolesAction;
import com.infinities.keystone4j.assignment.controller.action.role.v3.UpdateRoleAction;
import com.infinities.keystone4j.assignment.controller.callback.CheckGrantCollectionProtectionCallback;
import com.infinities.keystone4j.assignment.controller.callback.CheckGrantProtectionCallback;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.controller.action.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.controller.action.decorator.ProtectedCollectionDecorator;
import com.infinities.keystone4j.controller.action.decorator.ProtectedDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers.RoleV3 20141209

public class RoleV3ControllerImpl extends BaseController implements RoleV3Controller {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	// private static final boolean INHERITED = false;

	public RoleV3ControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
		this.assignmentApi.setIdentityApi(identityApi);
	}

	@Override
	public MemberWrapper<Role> createRole(Role role) throws Exception {
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CreateRoleAction(assignmentApi, tokenProviderApi,
				policyApi, role), tokenProviderApi, policyApi, null, role);
		MemberWrapper<Role> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Role> listRoles() throws Exception {
		FilterProtectedAction<Role> command = new FilterProtectedDecorator<Role>(new ListRolesAction(assignmentApi,
				tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		CollectionWrapper<Role> ret = command.execute(getRequest(), "name");
		return ret;
	}

	@Override
	public MemberWrapper<Role> getRole(String roleid) throws Exception {
		Role ref = getMemberFromDriver(roleid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new GetRoleAction(assignmentApi, tokenProviderApi,
				policyApi, roleid), tokenProviderApi, policyApi, ref, null);
		MemberWrapper<Role> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public MemberWrapper<Role> updateRole(String roleid, Role role) throws Exception {
		Role ref = getMemberFromDriver(roleid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new UpdateRoleAction(assignmentApi, tokenProviderApi,
				policyApi, roleid, role), tokenProviderApi, policyApi, ref, role);
		MemberWrapper<Role> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteRole(String roleid) throws Exception {
		Role ref = getMemberFromDriver(roleid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new DeleteRoleAction(assignmentApi, tokenProviderApi,
				policyApi, roleid), tokenProviderApi, policyApi, ref, null);
		command.execute(getRequest());
	}

	public Role getMemberFromDriver(String roleid) throws Exception {
		return assignmentApi.getRole(roleid);
	}

	@Override
	public CollectionWrapper<Role> listGrants(String userid, String groupid, String domainid, String projectid)
			throws Exception {
		CheckGrantCollectionProtectionCallback callback = new CheckGrantCollectionProtectionCallback(tokenProviderApi,
				policyApi, identityApi, assignmentApi, null, userid, groupid, domainid, projectid);
		FilterProtectedAction<Role> command = new ProtectedCollectionDecorator<Role>(new ListGrantsAction(assignmentApi,
				identityApi, tokenProviderApi, policyApi, userid, groupid, domainid, projectid), callback, tokenProviderApi,
				policyApi);
		return command.execute(getRequest());
	}

	@Override
	public void createGrant(String roleid, String userid, String groupid, String domainid, String projectid)
			throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, roleid, userid, groupid, domainid, projectid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CreateGrantAction(assignmentApi, identityApi,
				tokenProviderApi, policyApi, roleid, userid, groupid, domainid, projectid), callback, tokenProviderApi,
				policyApi);
		command.execute(getRequest());
	}

	@Override
	public void checkGrant(String roleid, String userid, String groupid, String domainid, String projectid) throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, roleid, userid, groupid, domainid, projectid);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new CheckGrantAction(assignmentApi, identityApi,
				tokenProviderApi, policyApi, roleid, userid, groupid, domainid, projectid), callback, tokenProviderApi,
				policyApi);
		command.execute(getRequest());
	}

	@Override
	public void revokeGrant(String roleid, String userid, String groupid, String domainid, String projectid)
			throws Exception {
		CheckGrantProtectionCallback callback = new CheckGrantProtectionCallback(tokenProviderApi, policyApi, identityApi,
				assignmentApi, roleid, userid, groupid, domainid, projectid, true);
		ProtectedAction<Role> command = new ProtectedDecorator<Role>(new DeleteGrantAction(assignmentApi, identityApi,
				tokenProviderApi, policyApi, roleid, userid, groupid, domainid, projectid), callback, tokenProviderApi,
				policyApi);
		command.execute(getRequest());
	}

}
