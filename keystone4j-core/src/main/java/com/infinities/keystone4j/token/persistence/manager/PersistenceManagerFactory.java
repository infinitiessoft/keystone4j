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
package com.infinities.keystone4j.token.persistence.manager;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.trust.TrustApi;

//keystone.policy.controllers.PolicyV3 20141211

public class PersistenceManagerFactory implements Factory<PersistenceManager> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final TokenDriver tokenDriver;


	@Inject
	public PersistenceManagerFactory(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenDriver tokenDriver) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.tokenDriver = tokenDriver;
	}

	@Override
	public void dispose(PersistenceManager arg0) {

	}

	@Override
	public PersistenceManager provide() {
		return new PersistenceManagerImpl(assignmentApi, identityApi, trustApi, tokenDriver);
	}

}
