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
package com.infinities.keystone4j.trust.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.model.trust.wrapper.TrustWrapper;
import com.infinities.keystone4j.model.trust.wrapper.TrustsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class GetTrustAction extends AbstractTrustAction<Trust> implements ProtectedAction<Trust> {

	private final String trustid;


	public GetTrustAction(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi, String trustid) {
		super(assignmentApi, identityApi, trustApi, tokenProviderApi, policyApi);
		this.trustid = trustid;
	}

	@Override
	public MemberWrapper<Trust> execute(ContainerRequestContext request) throws Exception {
		Trust trust = getTrust(request, trustid);
		return wrapMember(request, trust);
	}

	@Override
	public String getName() {
		return "get_trust";
	}

	@Override
	public CollectionWrapper<Trust> getCollectionWrapper() {
		return new TrustsWrapper();
	}

	@Override
	public MemberWrapper<Trust> getMemberWrapper() {
		return new TrustWrapper();
	}
}
