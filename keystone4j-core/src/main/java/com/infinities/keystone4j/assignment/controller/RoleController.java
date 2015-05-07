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
