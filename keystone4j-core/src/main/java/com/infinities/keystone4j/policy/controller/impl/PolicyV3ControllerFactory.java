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
package com.infinities.keystone4j.policy.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.policy.controllers.PolicyV3 20141211

public class PolicyV3ControllerFactory extends BaseControllerFactory implements Factory<PolicyV3Controller> {

	private final PolicyApi policyApi;
	private final TokenProviderApi tokenProviderApi;


	@Inject
	public PolicyV3ControllerFactory(PolicyApi policyApi, TokenProviderApi tokenProviderApi) {
		this.policyApi = policyApi;
		this.tokenProviderApi = tokenProviderApi;
	}

	@Override
	public void dispose(PolicyV3Controller arg0) {

	}

	@Override
	public PolicyV3Controller provide() {
		PolicyV3ControllerImpl controller = new PolicyV3ControllerImpl(policyApi, tokenProviderApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
