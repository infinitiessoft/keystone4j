package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.RoleAssignment;
import com.infinities.keystone4j.model.assignment.RoleAssignmentWrapper;

public interface RoleAssignmentV3Controller {

	CollectionWrapper<RoleAssignment> listRoleAssignments() throws Exception;

	RoleAssignmentWrapper getRoleAssignment();

	RoleAssignmentWrapper updateRoleAssignment();

	void deleteRoleAssignment();

}
