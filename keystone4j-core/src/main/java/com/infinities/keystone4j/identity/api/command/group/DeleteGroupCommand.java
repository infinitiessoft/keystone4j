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
package com.infinities.keystone4j.identity.api.command.group;

import java.util.ArrayList;
import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.Group;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteGroupCommand extends AbstractIdentityCommand implements NotifiableCommand<Group> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String groupid;


	public DeleteGroupCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String groupid) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.groupid = groupid;
	}

	@Override
	public Group execute() throws Exception {
		DomainIdDriverAndEntityId domainIdDriverAndEntityId = getDomainDriverAndEntityId(groupid);
		// String domainId = domainIdDriverAndEntityId.getDomainId();
		IdentityDriver driver = domainIdDriverAndEntityId.getDriver();
		String entityId = domainIdDriverAndEntityId.getLocalId();
		List<String> userIds = new ArrayList<String>();

		for (User user : this.getIdentityApi().listUsersInGroup(groupid, null)) {
			userIds.add(user.getId());
		}

		driver.deleteGroup(entityId);
		this.getIdMappingApi().deleteIdMapping(groupid);
		this.getAssignmentApi().deleteGroup(groupid);
		for (String uid : userIds) {
			this.getIdentityApi().emitInvalidateUserTokenPersistence(uid);
		}
		return null;
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return groupid;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
