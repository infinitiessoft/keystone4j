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

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.policy.Policy;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeletePolicyAction extends AbstractPolicyAction implements ProtectedAction<Policy> {

	private final String policyid;


	public DeletePolicyAction(PolicyApi policyApi, TokenProviderApi tokenProviderApi, String policyid) {
		super(policyApi, tokenProviderApi);
		this.policyid = policyid;
	}

	@Override
	public MemberWrapper<Policy> execute(ContainerRequestContext request) throws Exception {
		this.getPolicyApi().deletePolicy(policyid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_polic";
	}

}
