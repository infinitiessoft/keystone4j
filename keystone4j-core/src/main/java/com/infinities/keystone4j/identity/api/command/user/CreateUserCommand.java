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
package com.infinities.keystone4j.identity.api.command.user;

import java.util.UUID;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.mapping.EntityType;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class CreateUserCommand extends AbstractIdentityCommand implements NotifiableCommand<User> {

	private final User user;


	public CreateUserCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, User user) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.user = user;
	}

	@Override
	public User execute() throws Exception {
		String domainid = user.getDomainId();
		this.getAssignmentApi().getDomain(domainid);

		IdentityDriver driver = selectIdentityDriver(domainid);
		User user = clearDomainIdIfDomainUnaware(driver, this.user);
		user.setId(UUID.randomUUID().toString());
		User ref = driver.createUser(user.getId(), user);

		return setDomainIdAndMapping(ref, domainid, driver, EntityType.USER);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return user;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
