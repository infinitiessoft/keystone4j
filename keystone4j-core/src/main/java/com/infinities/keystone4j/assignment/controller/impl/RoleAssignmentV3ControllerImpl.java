package com.infinities.keystone4j.assignment.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.assignment.controller.action.roleassignment.ListRoleAssignmentsAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.RoleAssignment;
import com.infinities.keystone4j.model.assignment.RoleAssignmentWrapper;
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
	public CollectionWrapper<RoleAssignment> listRoleAssignments() throws Exception {
		FilterProtectedAction<RoleAssignment> command = new FilterProtectedDecorator<RoleAssignment>(
				new ListRoleAssignmentsAction(assignmentApi, identityApi), tokenProviderApi, policyApi);
		CollectionWrapper<RoleAssignment> ret = command.execute(getRequest(), "group.id", "role.id", "scope.domain.id",
				"scope.project.id", "scope.OS-INHERIT:inherited_to", "user.id");
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
