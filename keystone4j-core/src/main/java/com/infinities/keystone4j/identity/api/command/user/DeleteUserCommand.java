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

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteUserCommand extends AbstractIdentityCommand implements NotifiableCommand<User> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String userid;


	public DeleteUserCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String userid, String domainid) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.userid = userid;
	}

	@Override
	public User execute() throws Exception {
		DomainIdDriverAndEntityId domainIdDriverAndEntityId = getDomainDriverAndEntityId(userid);
		// String domainId = domainIdDriverAndEntityId.getDomainId();
		IdentityDriver driver = domainIdDriverAndEntityId.getDriver();
		String entityId = domainIdDriverAndEntityId.getLocalId();
		driver.deleteUser(entityId);
		this.getAssignmentApi().deleteUser(userid);
		this.getCredentialApi().deleteCredentialsForUser(userid);
		this.getIdMappingApi().deleteIdMapping(userid);
		return null;
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return userid;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
