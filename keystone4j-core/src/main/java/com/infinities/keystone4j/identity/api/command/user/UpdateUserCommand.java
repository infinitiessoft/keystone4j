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

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.mapping.EntityType;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class UpdateUserCommand extends AbstractIdentityCommand implements NotifiableCommand<User> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String userid;
	private final User userRef;


	public UpdateUserCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String userid, User userRef) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.userRef = userRef;
		this.userid = userid;
	}

	@Override
	public User execute() throws Exception {
		User oldUserRef = this.getIdentityApi().getUser(userid);
		if (!Strings.isNullOrEmpty(userRef.getDomainId())) {
			this.getAssignmentApi().getDomain(userRef.getDomainId());
		}
		if (!Strings.isNullOrEmpty(userRef.getId())) {
			if (!userid.equals(userRef.getId())) {
				throw Exceptions.ValidationException.getInstance("Cannot change user ID");
			}
			userRef.setId("");
		}
		DomainIdDriverAndEntityId domainIdDriverAndEntityId = getDomainDriverAndEntityId(userid);
		String domainId = domainIdDriverAndEntityId.getDomainId();
		IdentityDriver driver = domainIdDriverAndEntityId.getDriver();
		String entityId = domainIdDriverAndEntityId.getLocalId();
		User user = clearDomainIdIfDomainUnaware(driver, userRef);
		User ref = driver.updateUser(entityId, user);

		boolean enabledChange = (user.getEnabled() == false) && user.getEnabled() != oldUserRef.getEnabled();

		if (enabledChange || !Strings.isNullOrEmpty(user.getPassword())) {
			this.getIdentityApi().emitInvalidateUserTokenPersistence(userid);
		}
		return setDomainIdAndMapping(ref, domainId, driver, EntityType.USER);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return userid;
		} else if (index == 2) {
			return userRef;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
