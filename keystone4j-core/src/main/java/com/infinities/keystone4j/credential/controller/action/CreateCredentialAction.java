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
package com.infinities.keystone4j.credential.controller.action;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.credential.Blob;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.JsonUtils;

public class CreateCredentialAction extends AbstractCredentialAction implements ProtectedAction<Credential> {

	private final static Logger logger = LoggerFactory.getLogger(CreateCredentialAction.class);
	private final Credential credential;


	public CreateCredentialAction(CredentialApi credentialApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			Credential credential) {
		super(credentialApi, tokenProviderApi, policyApi);
		this.credential = credential;
	}

	@Override
	public MemberWrapper<Credential> execute(ContainerRequestContext request) throws Exception {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		String trustId = getTrustIdForRequest(context);
		logger.debug("trust id from request: {}", trustId);
		Credential ref = assignUniqueId(credential, trustId);
		ref = credentialApi.createCredential(ref.getId(), ref);
		return this.wrapMember(request, ref);
	}

	// trustId = null
	private Credential assignUniqueId(Credential ref, String trustId) throws JsonGenerationException, JsonMappingException,
			IOException {
		if ("ec2".equals(ref.getType().toLowerCase())) {
			Blob blob = null;
			try {
				blob = JsonUtils.readJson(ref.getBlob(), new TypeReference<Blob>() {
				});
			} catch (Exception e) {
				throw Exceptions.ValidationException.getInstance("Invalid blob in credential");
			}

			if (blob == null) {
				throw Exceptions.ValidationException.getInstance(null, "blob", "credential");
			}

			if (Strings.isNullOrEmpty(blob.getAccess())) {
				throw Exceptions.ValidationException.getInstance(null, "access", "blob");
			}

			// logger.debug("blob id before hashing: {}", blob.getId());
			ref.setId(Cms.toHex(Hashing.sha256().hashString(blob.getAccess(), Charsets.UTF_16LE).asBytes()));
			// logger.debug("blob id after hashing: {}", blob.getId());
			if (!Strings.isNullOrEmpty(trustId)) {
				blob.setTrustId(trustId);
				ref.setBlob(JsonUtils.toJsonWithoutPrettyPrint(blob));
			}
			return ref;
		} else {
			return assignUniqueId(ref, null);
		}
	}

	@Override
	public String getName() {
		return "create_credential";
	}
}
