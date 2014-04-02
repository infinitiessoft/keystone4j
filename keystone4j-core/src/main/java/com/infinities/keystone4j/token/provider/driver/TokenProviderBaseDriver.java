package com.infinities.keystone4j.token.provider.driver;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Role;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDataHelper;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.model.TokenDataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.trust.model.Trust;

public abstract class TokenProviderBaseDriver implements TokenProviderDriver {

	private final static Logger logger = LoggerFactory.getLogger(TokenProviderBaseDriver.class);
	// private AssignmentApi assignmentApi;
	// private CatalogApi catalogApi;
	// private IdentityApi identityApi;
	private final TokenApi tokenApi;
	// private TrustApi trustApi;
	private final TokenDataHelper helper;


	public TokenProviderBaseDriver(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi,
			TokenApi tokenApi) {
		helper = new TokenDataHelper(identityApi, assignmentApi, catalogApi);
		this.tokenApi = tokenApi;
	}

	@Override
	public TokenMetadata issueV3Token(String userid, List<String> methodNames, Date expiresAt, String projectid,
			String domainid, AuthContext authContext, Trust trust, Token token, boolean includeCatalog) {
		boolean enabled = Config.Instance.getOpt(Config.Type.trust, "enabled").asBoolean();
		if (enabled && trust == null && token != null && token.getTrust() != null) {
			trust = token.getTrust();
		}

		if (methodNames.contains("oauth1")) {
			throw Exceptions.ForbiddenException.getInstance("Oauth is disabled.");
		}

		TokenDataWrapper tokenData = helper.getTokenData(userid, methodNames, expiresAt, projectid, domainid,
				authContext.getBind(), trust, token, includeCatalog);

		String tokenid = getTokenId(tokenData);
		if (token == null) {
			token = new Token();
		}

		List<Role> roles = Lists.newArrayList();
		if (tokenData.getToken().getProject() != null) {
			roles.addAll(tokenData.getToken().getRoles());
		}
		if (trust != null) {
			token.setTrust(trust);
		}
		token.setUser(tokenData.getToken().getUser());
		token.setTokenData(tokenData);
		token.setId(tokenid);
		token = this.tokenApi.createToken(token);
		tokenData.getToken().setToken(token);

		return new TokenMetadata(tokenid, tokenData);
	}

	@Override
	public void revokeToken(String tokenid) {
		this.tokenApi.deleteToken(tokenid);
	}

	@Override
	public TokenDataWrapper validateV3Token(String uniqueid) {
		Token token = this.verifyToken(uniqueid);
		logger.debug("validate token uniqueid: {}", uniqueid);
		TokenDataWrapper tokenData = validateV3TokenRef(token);

		return tokenData;
	}

	private Token verifyToken(String tokenid) {
		Token token = this.tokenApi.getToken(tokenid);
		return verifyTokenRef(token);
	}

	private Token verifyTokenRef(Token token) {
		if (token == null) {
			throw Exceptions.UnauthorizedException.getInstance();
		}
		return token;

	}

	private TokenDataWrapper validateV3TokenRef(Token token) {
		// Project project = token.getTrust().getProject();
		List<String> methodNames = Lists.newArrayList();
		methodNames.add("password");
		methodNames.add("token");

		String projectid = null;
		if (token.getProject() != null) {
			projectid = token.getProject().getId();
		}
		logger.debug("validate token");
		return this.helper.getTokenData(token.getUser().getId(), methodNames, token.getExpires(), projectid, null,
				token.getBind(), null, null, true);
	}

	protected abstract String getTokenId(TokenDataWrapper tokenData);

}
