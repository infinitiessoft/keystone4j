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

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.controller.action.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.controller.action.decorator.ProtectedDecorator;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.policy.controller.PolicyV3Controller;
import com.infinities.keystone4j.policy.controller.action.CreatePolicyAction;
import com.infinities.keystone4j.policy.controller.action.DeletePolicyAction;
import com.infinities.keystone4j.policy.controller.action.GetPolicyAction;
import com.infinities.keystone4j.policy.controller.action.ListPoliciesAction;
import com.infinities.keystone4j.policy.controller.action.UpdatePolicyAction;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.policy.controllers.PolicyV3 20141211

public class PolicyV3ControllerImpl extends BaseController implements PolicyV3Controller {

	private final PolicyApi policyApi;
	private final TokenProviderApi tokenProviderApi;


	public PolicyV3ControllerImpl(PolicyApi policyApi, TokenProviderApi tokenProviderApi) {
		this.policyApi = policyApi;
		this.tokenProviderApi = tokenProviderApi;
	}

	// TODO Ignore validation.validated(schema.policy_create,'policy')
	@Override
	public MemberWrapper<Policy> createPolicy(Policy policy) throws Exception {
		ProtectedAction<Policy> command = new ProtectedDecorator<Policy>(new CreatePolicyAction(policyApi, tokenProviderApi,
				policy), tokenProviderApi, policyApi);
		MemberWrapper<Policy> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Policy> listPolicies() throws Exception {
		FilterProtectedAction<Policy> command = new FilterProtectedDecorator<Policy>(new ListPoliciesAction(policyApi,
				tokenProviderApi), tokenProviderApi, policyApi);
		CollectionWrapper<Policy> ret = command.execute(getRequest(), "type");
		return ret;
	}

	@Override
	public MemberWrapper<Policy> getPolicy(String policyid) throws Exception {
		ProtectedAction<Policy> command = new ProtectedDecorator<Policy>(new GetPolicyAction(policyApi, tokenProviderApi,
				policyid), tokenProviderApi, policyApi);
		MemberWrapper<Policy> ret = command.execute(getRequest());
		return ret;
	}

	// TODO Ignore validation.validated(schema.policy_update,'policy')
	@Override
	public MemberWrapper<Policy> updatePolicy(String policyid, Policy policy) throws Exception {
		ProtectedAction<Policy> command = new ProtectedDecorator<Policy>(new UpdatePolicyAction(policyApi, tokenProviderApi,
				policyid, policy), tokenProviderApi, policyApi);
		MemberWrapper<Policy> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deletePolicy(String policyid) throws Exception {
		ProtectedAction<Policy> command = new ProtectedDecorator<Policy>(new DeletePolicyAction(policyApi, tokenProviderApi,
				policyid), tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

}
