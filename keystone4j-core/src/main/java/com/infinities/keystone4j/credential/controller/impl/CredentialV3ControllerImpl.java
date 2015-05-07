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
package com.infinities.keystone4j.credential.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.controller.action.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.controller.action.decorator.ProtectedDecorator;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.controller.CredentialV3Controller;
import com.infinities.keystone4j.credential.controller.action.CreateCredentialAction;
import com.infinities.keystone4j.credential.controller.action.DeleteCredentialAction;
import com.infinities.keystone4j.credential.controller.action.GetCredentialAction;
import com.infinities.keystone4j.credential.controller.action.ListCredentialsAction;
import com.infinities.keystone4j.credential.controller.action.UpdateCredentialAction;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CredentialV3ControllerImpl extends BaseController implements CredentialV3Controller {

	private final CredentialApi credentialApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public CredentialV3ControllerImpl(CredentialApi credentialApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.credentialApi = credentialApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	// TODO ignore @validation.validated(schema.credential_create, 'credential')
	@Override
	public MemberWrapper<Credential> createCredential(Credential credential) throws Exception {
		ProtectedAction<Credential> command = new ProtectedDecorator<Credential>(new CreateCredentialAction(credentialApi,
				tokenProviderApi, policyApi, credential), tokenProviderApi, policyApi, null, credential);
		MemberWrapper<Credential> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Credential> listCredentials() throws Exception {
		FilterProtectedAction<Credential> command = new FilterProtectedDecorator<Credential>(new ListCredentialsAction(
				credentialApi, tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		CollectionWrapper<Credential> ret = command.execute(getRequest(), "user_id");
		return ret;
	}

	@Override
	public MemberWrapper<Credential> getCredential(String credentialid) throws Exception {
		Credential ref = getMemberFromDriver(credentialid);
		ProtectedAction<Credential> command = new ProtectedDecorator<Credential>(new GetCredentialAction(credentialApi,
				tokenProviderApi, policyApi, credentialid), tokenProviderApi, policyApi, ref, null);
		MemberWrapper<Credential> ret = command.execute(getRequest());
		return ret;
	}

	// TODO ignore @validation.validated(schema.credential_update, 'credential')
	@Override
	public MemberWrapper<Credential> updateCredential(String credentialid, Credential credential) throws Exception {
		Credential ref = getMemberFromDriver(credentialid);
		ProtectedAction<Credential> command = new ProtectedDecorator<Credential>(new UpdateCredentialAction(credentialApi,
				tokenProviderApi, policyApi, credentialid, credential), tokenProviderApi, policyApi, ref, credential);
		MemberWrapper<Credential> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteCredential(String credentialid) throws Exception {
		Credential ref = getMemberFromDriver(credentialid);
		ProtectedAction<Credential> command = new ProtectedDecorator<Credential>(new DeleteCredentialAction(credentialApi,
				tokenProviderApi, policyApi, credentialid), tokenProviderApi, policyApi, ref, null);
		command.execute(getRequest());
	}

	public Credential getMemberFromDriver(String credentialid) throws Exception {
		return credentialApi.getCredential(credentialid);
	}
}
