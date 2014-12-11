package com.infinities.keystone4j.token.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.decorator.ProtectedDecorator;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.token.Auth;
import com.infinities.keystone4j.model.token.v2.EndpointsV2Wrapper;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.model.trust.SignedWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.controller.TokenController;
import com.infinities.keystone4j.token.controller.action.AbstractTokenAction;
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
	private final TokenApi tokenApi;
	private final TokenProviderApi tokenProviderApi;
	private final TrustApi trustApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public TokenV2ControllerImpl(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenApi tokenApi, TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi) {
		super();
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
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
	public TokenV2DataWrapper authenticate(Auth auth) {
		AbstractTokenAction<TokenV2DataWrapper> command = new AuthenticateAction(assignmentApi, catalogApi, identityApi,
				tokenApi, tokenProviderApi, trustApi, auth);
		TokenV2DataWrapper ret = command.execute(getRequest());
		logger.debug("generate token id: {}", ret.getAccess().getToken().getId());
		return ret;
	}

	@Override
	public void validateTokenHead(String tokenid, String belongsTo) {
		ProtectedAction<TokenV2DataWrapper> command = new ProtectedDecorator<TokenV2DataWrapper>(new ValidateTokenHeadAction(
				assignmentApi, catalogApi, identityApi, tokenApi, tokenProviderApi, trustApi, tokenid, belongsTo), null,
				tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

	@Override
	public TokenV2DataWrapper validateToken(String tokenid, String belongsTo) {
		ProtectedAction<TokenV2DataWrapper> command = new ProtectedDecorator<TokenV2DataWrapper>(new ValidateTokenAction(
				assignmentApi, catalogApi, identityApi, tokenApi, tokenProviderApi, trustApi, tokenid, belongsTo), null,
				tokenApi, policyApi, parMap);
		return command.execute(getRequest());
	}

	@Override
	public void deleteToken(String tokenid) {
		AbstractTokenAction<Boolean> command = new DeleteTokenAction(assignmentApi, catalogApi, identityApi, tokenApi,
				tokenProviderApi, trustApi, policyApi, tokenid);
		command.execute(getRequest());
	}

	@Override
	public SignedWrapper getRevocationList() {
		ProtectedAction<SignedWrapper> command = new ProtectedDecorator<SignedWrapper>(new GetRevocationListAction(assignmentApi,
				catalogApi, identityApi, tokenApi, tokenProviderApi, trustApi), null, tokenApi, policyApi, parMap);
		return command.execute(getRequest());
	}

	@Override
	public EndpointsV2Wrapper getEndpoints(String tokenid) {
		AbstractTokenAction<EndpointsV2Wrapper> command = new ListEndpointsAction(assignmentApi, catalogApi, identityApi,
				tokenApi, tokenProviderApi, trustApi, policyApi, tokenid);
		return command.execute(getRequest());
	}

	private Response getCertificate(String text) {
		try {
			// URL url = getClass().getResource(text);
			// File file = new File(text);
			URL url = new KeystoneUtils().getURL(text);
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
