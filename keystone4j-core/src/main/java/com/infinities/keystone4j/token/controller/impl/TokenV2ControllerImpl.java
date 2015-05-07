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
package com.infinities.keystone4j.token.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.controller.action.decorator.ProtectedDecorator;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.token.Auth;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.Access.Service;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.controller.TokenController;
import com.infinities.keystone4j.token.controller.action.AuthenticateAction;
import com.infinities.keystone4j.token.controller.action.DeleteTokenAction;
import com.infinities.keystone4j.token.controller.action.GetRevocationListAction;
import com.infinities.keystone4j.token.controller.action.ListEndpointsAction;
import com.infinities.keystone4j.token.controller.action.ValidateTokenAction;
import com.infinities.keystone4j.token.controller.action.ValidateTokenHeadAction;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class TokenV2ControllerImpl extends BaseController implements TokenController {

	private final static Logger logger = LoggerFactory.getLogger(TokenV2ControllerImpl.class);
	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final TrustApi trustApi;
	private final PolicyApi policyApi;


	public TokenV2ControllerImpl(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi) {
		super();
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.policyApi = policyApi;
		this.assignmentApi.setIdentityApi(identityApi);
	}

	@Override
	public Response getCaCert() {
		return getCertificate(Config.Instance.getOpt(Config.Type.signing, "ca_certs").asText());
	}

	@Override
	public Response getSigningCert() {
		return getCertificate(Config.Instance.getOpt(Config.Type.signing, "certfile").asText());
	}

	@Override
	public MemberWrapper<Access> authenticate(Auth auth) throws Exception {
		AuthenticateAction command = new AuthenticateAction(assignmentApi, catalogApi, identityApi, tokenProviderApi,
				trustApi, policyApi, auth);
		MemberWrapper<Access> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void validateTokenHead(String tokenid) throws Exception {
		ProtectedAction<Access> command = new ProtectedDecorator<Access>(new ValidateTokenHeadAction(assignmentApi,
				catalogApi, identityApi, tokenProviderApi, trustApi, policyApi, tokenid), tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public MemberWrapper<Access> validateToken(String tokenid) throws Exception {
		ProtectedAction<Access> command = new ProtectedDecorator<Access>(new ValidateTokenAction(assignmentApi, catalogApi,
				identityApi, tokenProviderApi, trustApi, policyApi, tokenid), tokenProviderApi, policyApi);
		return command.execute(getRequest());
	}

	@Override
	public void deleteToken(String tokenid) throws Exception {
		DeleteTokenAction command = new DeleteTokenAction(assignmentApi, catalogApi, identityApi, tokenProviderApi,
				trustApi, policyApi, tokenid);
		command.execute(getRequest());
	}

	@Override
	public MemberWrapper<String> getRevocationList() throws Exception {
		ProtectedAction<String> command = new ProtectedDecorator<String>(new GetRevocationListAction(assignmentApi,
				catalogApi, identityApi, tokenProviderApi, trustApi, policyApi), tokenProviderApi, policyApi);
		return command.execute(getRequest());
	}

	@Override
	public Service getEndpoints(String tokenid) throws Exception {
		ListEndpointsAction command = new ListEndpointsAction(assignmentApi, catalogApi, identityApi, tokenProviderApi,
				trustApi, policyApi, tokenid);
		return command.execute(getRequest());
	}

	private Response getCertificate(String text) {
		try {
			// URL url = getClass().getResource(text);
			// File file = new File(text);
			URL url = KeystoneUtils.getURL(text);
			return Response.status(200).type(MediaType.TEXT_HTML).entity(getBytesFromFile(url)).build();
		} catch (Exception e) {
			throw Exceptions.CertificateFilesUnavailableException.getInstance(null);
		}
	}

	private byte[] getBytesFromFile(URL url) throws IOException {
		InputStream is = null;
		try {
			File file = new File(url.getPath());
			is = new FileInputStream(file);

			long length = file.length();

			if (length > Integer.MAX_VALUE) {
				logger.warn("File: {} is too large", file.getName());
				throw new RuntimeException("file is too large " + file.getName());
			}

			byte[] bytes = new byte[(int) length];
			int offset = 0;
			int numRead = 0;

			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getName());
			}

			return bytes;
		} finally {
			is.close();
		}
	}

}
