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

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.User;

public class ChangePasswordCommand extends AbstractIdentityCommand implements NonTruncatedCommand<User> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AuthenticateCommand.class);
	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String userid;
	private final String originalPassword;
	private final String newPassword;


	public ChangePasswordCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String userid,
			String originalPassword, String newPassword) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.userid = userid;
		this.originalPassword = originalPassword;
		this.newPassword = newPassword;
	}

	@Override
	public User execute() throws Exception {
		this.getIdentityApi().authenticate(userid, originalPassword);
		User updateDict = new User();
		updateDict.setPassword(newPassword);
		this.getIdentityApi().updateUser(userid, updateDict);
		return null;
	}
}
