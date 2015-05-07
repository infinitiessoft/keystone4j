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
package com.infinities.keystone4j.credential.api;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.api.command.decorator.ResponseTruncatedCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.api.command.CreateCredentialCommand;
import com.infinities.keystone4j.credential.api.command.DeleteCredentialCommand;
import com.infinities.keystone4j.credential.api.command.DeleteCredentialsForProjectCommand;
import com.infinities.keystone4j.credential.api.command.DeleteCredentialsForUserCommand;
import com.infinities.keystone4j.credential.api.command.GetCredentialCommand;
import com.infinities.keystone4j.credential.api.command.ListCredentialsCommand;
import com.infinities.keystone4j.credential.api.command.ListCredentialsForUserCommand;
import com.infinities.keystone4j.credential.api.command.UpdateCredentialCommand;
import com.infinities.keystone4j.model.credential.Credential;

public class CredentialApiImpl implements CredentialApi {

	private final CredentialDriver credentialDriver;


	public CredentialApiImpl(CredentialDriver credentialDriver) {
		super();
		this.credentialDriver = credentialDriver;
	}

	@Override
	public Credential createCredential(String credentialid, Credential credential) throws Exception {
		CreateCredentialCommand command = new CreateCredentialCommand(credentialDriver, credentialid, credential);
		return command.execute();
	}

	@Override
	public List<Credential> listCredentials(Hints hints) throws Exception {
		TruncatedCommand<Credential> command = new ResponseTruncatedCommand<Credential>(new ListCredentialsCommand(
				credentialDriver), credentialDriver);
		return command.execute(hints);
	}

	@Override
	public List<Credential> listCredentialsForUser(String userId) throws Exception {
		ListCredentialsForUserCommand command = new ListCredentialsForUserCommand(credentialDriver, userId);
		return command.execute();
	}

	@Override
	public Credential getCredential(String credentialid) throws Exception {
		GetCredentialCommand command = new GetCredentialCommand(credentialDriver, credentialid);
		return command.execute();
	}

	@Override
	public Credential updateCredential(String credentialid, Credential credential) throws Exception {
		UpdateCredentialCommand command = new UpdateCredentialCommand(credentialDriver, credentialid, credential);
		return command.execute();
	}

	@Override
	public Credential deleteCredential(String credentialid) throws Exception {
		DeleteCredentialCommand command = new DeleteCredentialCommand(credentialDriver, credentialid);
		return command.execute();
	}

	@Override
	public void deleteCredentialsForProject(String projectid) throws Exception {
		DeleteCredentialsForProjectCommand command = new DeleteCredentialsForProjectCommand(credentialDriver, projectid);
		command.execute();
	}

	@Override
	public void deleteCredentialsForUser(String userid) throws Exception {
		DeleteCredentialsForUserCommand command = new DeleteCredentialsForUserCommand(credentialDriver, userid);
		command.execute();
	}

}
