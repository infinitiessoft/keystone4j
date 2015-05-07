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
package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers.RoleAssignmentV3 20141210

public class RoleAssignmentV3ControllerFactory extends BaseControllerFactory implements Factory<RoleAssignmentV3Controller> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	@Inject
	public RoleAssignmentV3ControllerFactory(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.assignmentApi.setIdentityApi(identityApi);
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(RoleAssignmentV3Controller arg0) {

	}

	@Override
	public RoleAssignmentV3Controller provide() {
		RoleAssignmentV3ControllerImpl controller = new RoleAssignmentV3ControllerImpl(assignmentApi, identityApi,
				tokenProviderApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
