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
package com.infinities.keystone4j.credential.api.command;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class CreateCredentialCommand extends AbstractCredentialCommand implements NonTruncatedCommand<Credential> {

	private final String credentialid;
	private final Credential credential;


	public CreateCredentialCommand(CredentialDriver credentialDriver, String credentialid, Credential credential) {
		super(credentialDriver);
		this.credentialid = credentialid;
		this.credential = credential;
	}

	@Override
	public Credential execute() throws Exception {
		return this.getCredentialDriver().createCredential(credentialid, credential);
	}

}
