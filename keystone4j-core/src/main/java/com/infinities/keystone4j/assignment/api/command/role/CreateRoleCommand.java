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
package com.infinities.keystone4j.assignment.api.command.role;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class CreateRoleCommand extends AbstractAssignmentCommand implements NotifiableCommand<Role> {

	private final String roleid;
	private final Role role;


	public CreateRoleCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String roleid, Role role) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.roleid = roleid;
		this.role = role;
	}

	@Override
	public Role execute() {
		return this.getAssignmentDriver().createRole(roleid, role);

		// TODO ignore SHOULD_CACHE
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return roleid;
		} else if (index == 2) {
			return role;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
