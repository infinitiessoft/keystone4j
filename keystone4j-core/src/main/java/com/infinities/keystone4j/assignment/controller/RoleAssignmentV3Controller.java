package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.assignment.model.RoleAssignmentWrapper;
import com.infinities.keystone4j.assignment.model.RoleAssignmentsWrapper;

public interface RoleAssignmentV3Controller {

	RoleAssignmentsWrapper listRoleAssignments();

	RoleAssignmentWrapper getRoleAssignment();

	RoleAssignmentWrapper updateRoleAssignment();

	void deleteRoleAssignment();

}
