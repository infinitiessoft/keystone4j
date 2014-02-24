package com.infinities.keystone4j.token.provider.api;

import java.util.Date;
import java.util.List;

import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.model.TokenDataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.token.provider.command.CheckV3TokenCommand;
import com.infinities.keystone4j.token.provider.command.IssueV3TokenCommand;
import com.infinities.keystone4j.token.provider.command.RevokeTokenCommand;
import com.infinities.keystone4j.token.provider.command.ValidateV3TokenCommand;
import com.infinities.keystone4j.trust.model.Trust;

public class TokenProviderApiImpl implements TokenProviderApi {

	private final TokenApi tokenApi;
	private final TokenProviderDriver tokenProviderDriver;


	public TokenProviderApiImpl(TokenApi tokenApi, TokenProviderDriver tokenProviderDriver) {
		super();
		this.tokenApi = tokenApi;
		this.tokenProviderDriver = tokenProviderDriver;
	}

	@Override
	public TokenMetadata issueV3Token(String userid, List<String> methodNames, Date expiresAt, String projectid,
			String domainid, AuthContext authContext, Trust trust, Token token, boolean includeCatalog) {
		IssueV3TokenCommand command = new IssueV3TokenCommand(tokenApi, this, tokenProviderDriver, userid, methodNames,
				expiresAt, projectid, domainid, authContext, trust, token, includeCatalog);
		return command.execute();
	}

	@Override
	public void checkV3Token(String tokenid) {
		CheckV3TokenCommand command = new CheckV3TokenCommand(tokenApi, this, tokenProviderDriver, tokenid);
		command.execute();
	}

	@Override
	public void revokeToken(String tokenid) {
		RevokeTokenCommand command = new RevokeTokenCommand(tokenApi, this, tokenProviderDriver, tokenid);
		command.execute();
	}

	@Override
	public TokenDataWrapper validateV3Token(String tokenid) {
		ValidateV3TokenCommand command = new ValidateV3TokenCommand(tokenApi, this, tokenProviderDriver, tokenid);
		return command.execute();
	}

}
