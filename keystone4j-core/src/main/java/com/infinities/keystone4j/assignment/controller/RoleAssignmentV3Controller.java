package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.FormattedRoleAssignment;
import com.infinities.keystone4j.model.assignment.wrapper.RoleAssignmentWrapper;

public interface RoleAssignmentV3Controller {

	CollectionWrapper<FormattedRoleAssignment> listRoleAssignments() throws Exception;

	RoleAssignmentWrapper getRoleAssignment();

	RoleAssignmentWrapper updateRoleAssignment();

	void deleteRoleAssignment();

}
