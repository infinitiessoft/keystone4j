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
package com.infinities.keystone4j.policy.controller.action;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.model.policy.wrapper.PoliciesWrapper;
import com.infinities.keystone4j.model.policy.wrapper.PolicyWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractPolicyAction extends AbstractAction<Policy> {

	protected PolicyApi policyApi;


	public AbstractPolicyAction(PolicyApi policyApi, TokenProviderApi tokenProviderApi) {
		super(tokenProviderApi, policyApi);
		this.policyApi = policyApi;
	}

	public PolicyApi getPolicyApi() {
		return policyApi;
	}

	public void setPolicyApi(PolicyApi policyApi) {
		this.policyApi = policyApi;
	}

	@Override
	public CollectionWrapper<Policy> getCollectionWrapper() {
		return new PoliciesWrapper();
	}

	@Override
	public MemberWrapper<Policy> getMemberWrapper() {
		return new PolicyWrapper();
	}

	@Override
	public String getCollectionName() {
		return "policies";
	}

	@Override
	public String getMemberName() {
		return "policy";
	}

}
