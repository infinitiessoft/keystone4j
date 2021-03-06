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
package com.infinities.keystone4j.auth.controller.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.auth.RevokedWrapper;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.trust.wrapper.SignedWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.JsonUtils;

public class GetRevocationListAction extends AbstractAuthAction implements ProtectedAction<String> {

	public GetRevocationListAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi);
	}

	@Override
	public SignedWrapper execute(ContainerRequestContext request) throws Exception {
		if (!Config.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
			throw Exceptions.Gone.getInstance();
		}

		List<Token> tokens = this.getTokenProviderApi().listRevokedTokens();

		RevokedWrapper revoked = new RevokedWrapper(tokens);
		String jsonData = null;
		String signedText = null;
		try {
			String certfile = Config.getOpt(Config.Type.signing, "certfile").asText();
			String keyfile = Config.getOpt(Config.Type.signing, "keyfile").asText();

			jsonData = JsonUtils.toJson(revoked);
			signedText = Cms.signText(jsonData, certfile, keyfile);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new SignedWrapper(signedText);
	}

	@Override
	public String getName() {
		return "revocation_list";
	}

	@Override
	public MemberWrapper<String> getMemberWrapper() {
		return new SignedWrapper();
	}
}
