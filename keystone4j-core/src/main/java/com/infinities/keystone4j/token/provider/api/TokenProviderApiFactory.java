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
package com.infinities.keystone4j.token.provider.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.trust.TrustApi;

public class TokenProviderApiFactory implements Factory<TokenProviderApi> {

	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final PersistenceManager persistenceManager;
	private final RevokeApi revokeApi;
	private final TokenProviderDriver tokenProviderDriver;


	@Inject
	public TokenProviderApiFactory(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TrustApi trustApi, RevokeApi revokeApi, TokenProviderDriver tokenProviderDriver,
			PersistenceManager persistenceManager) {
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.revokeApi = revokeApi;
		this.tokenProviderDriver = tokenProviderDriver;
		this.persistenceManager = persistenceManager;
	}

	@Override
	public void dispose(TokenProviderApi arg0) {

	}

	@Override
	public TokenProviderApi provide() {
		TokenProviderApi tokenProviderApi;
		try {
			tokenProviderApi = new TokenProviderApiImpl(assignmentApi, catalogApi, identityApi, trustApi, revokeApi,
					tokenProviderDriver, persistenceManager);
			return tokenProviderApi;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
