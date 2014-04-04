package com.infinities.keystone4j.assignment.controller.impl;

import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.assignment.RoleAssignmentWrapper;
import com.infinities.keystone4j.model.assignment.RoleAssignmentsWrapper;

public class RoleAssignmentV3ControllerImpl extends BaseController implements RoleAssignmentV3Controller {

	@Override
	public RoleAssignmentsWrapper listRoleAssignments() {
		// TODO not implemented yet
		throw Exceptions.NotImplementedException.getInstance();
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
