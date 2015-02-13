package com.infinities.keystone4j.assignment.controller;

import com.infinities.keystone4j.model.assignment.wrapper.RoleWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RolesWrapper;

public interface RoleController {

	RolesWrapper getUserRoles(String userid, String tenantid);

	RoleWrapper getRole();

	RoleWrapper createRole();

	void deleteRole();

	RolesWrapper getRoles();

	RoleWrapper addRoleToUser();

	void removeRoleFromUser();

	RolesWrapper getRoleRefs();

	RoleWrapper createRoleRef();

	void deleteRoleRef();

}
