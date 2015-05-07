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
package com.infinities.keystone4j.auth.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class AuthControllerFactory extends BaseControllerFactory implements Factory<AuthController> {

	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final PolicyApi policyApi;
	private final CatalogApi catalogApi;


	@Inject
	public AuthControllerFactory(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(AuthController arg0) {

	}

	@Override
	public AuthController provide() {
		AuthControllerImpl controller = new AuthControllerImpl(assignmentApi, catalogApi, identityApi, tokenProviderApi,
				trustApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
