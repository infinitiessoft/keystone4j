package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.model.assignment.RoleAssignmentWrapper;
import com.infinities.keystone4j.model.assignment.RoleAssignmentsWrapper;

public interface RoleAssignmentV3Controller {

	RoleAssignmentsWrapper listRoleAssignments();

	RoleAssignmentWrapper getRoleAssignment();

	RoleAssignmentWrapper updateRoleAssignment();

	void deleteRoleAssignment();

}
