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
package com.infinities.keystone4j.identity.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;

public class IdentityApiFactory implements Factory<IdentityApi> {

	private final CredentialApi credentialApi;
	private final IdentityDriver identityDriver;
	private final RevokeApi revokeApi;
	private final IdMappingApi idMappingApi;


	@Inject
	public IdentityApiFactory(RevokeApi revokeApi, CredentialApi credentialApi, IdMappingApi idMappingApi,
			IdentityDriver identityDriver) {
		this.credentialApi = credentialApi;
		this.identityDriver = identityDriver;
		this.revokeApi = revokeApi;
		this.idMappingApi = idMappingApi;
	}

	@Override
	public void dispose(IdentityApi arg0) {

	}

	@Override
	public IdentityApi provide() {
		IdentityApi identityApi = new IdentityApiImpl(credentialApi, revokeApi, idMappingApi, identityDriver);
		return identityApi;
	}

}
