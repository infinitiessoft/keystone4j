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
package com.infinities.keystone4j.credential.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetCredentialAction extends AbstractCredentialAction implements ProtectedAction<Credential> {

	private final String credentialid;


	public GetCredentialAction(CredentialApi credentialApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String credentialid) {
		super(credentialApi, tokenProviderApi, policyApi);
		this.credentialid = credentialid;
	}

	@Override
	public MemberWrapper<Credential> execute(ContainerRequestContext request) throws Exception {
		Credential ref = this.getCredentialApi().getCredential(credentialid);
		return this.wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "get_credential";
	}
}
