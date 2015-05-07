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
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.assignment.controller.action.roleassignment.ListRoleAssignmentsAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.controller.action.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.FormattedRoleAssignment;
import com.infinities.keystone4j.model.assignment.wrapper.RoleAssignmentWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers.RoleAssignmentV3 20141210

public class RoleAssignmentV3ControllerImpl extends BaseController implements RoleAssignmentV3Controller {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public RoleAssignmentV3ControllerImpl(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public CollectionWrapper<FormattedRoleAssignment> listRoleAssignments() throws Exception {
		FilterProtectedAction<FormattedRoleAssignment> command = new FilterProtectedDecorator<FormattedRoleAssignment>(
				new ListRoleAssignmentsAction(assignmentApi, identityApi), tokenProviderApi, policyApi);
		CollectionWrapper<FormattedRoleAssignment> ret = command.execute(getRequest(), "group.id", "role.id",
				"scope.domain.id", "scope.project.id", "scope.OS-INHERIT:inherited_to", "user.id");
		return ret;
	}

	@Override
	public RoleAssignmentWrapper getRoleAssignment() {
		// TODO not implemented yet
		throw Exceptions.NotImplementedException.getInstance();
	}

	@Override
	public RoleAssignmentWrapper updateRoleAssignment() {
		// TODO not implemented yet
		throw Exceptions.NotImplementedException.getInstance();
	}

	@Override
	public void deleteRoleAssignment() {
		// TODO not implemented yet
		throw Exceptions.NotImplementedException.getInstance();
	}
}
