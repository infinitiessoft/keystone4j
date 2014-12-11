package com.infinities.keystone4j.token.provider.api;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.token.IToken;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.token.provider.api.command.CheckV2TokenCommand;
import com.infinities.keystone4j.token.provider.api.command.CheckV3TokenCommand;
import com.infinities.keystone4j.token.provider.api.command.IssueV2TokenCommand;
import com.infinities.keystone4j.token.provider.api.command.IssueV3TokenCommand;
import com.infinities.keystone4j.token.provider.api.command.RevokeTokenCommand;
import com.infinities.keystone4j.token.provider.api.command.ValidateV2TokenCommand;
import com.infinities.keystone4j.token.provider.api.command.ValidateV3TokenCommand;

public class TokenProviderApiImpl implements TokenProviderApi {

	private final TokenProviderDriver tokenProviderDriver;


	public TokenProviderApiImpl(TokenProviderDriver tokenProviderDriver) {
		super();
		this.tokenProviderDriver = tokenProviderDriver;

	}

	@Override
	public TokenIdAndData issueV3Token(String userid, List<String> methodNames, Date expiresAt, String projectid,
			String domainid, AuthContext authContext, Trust trust, Token token, boolean includeCatalog) {
		IssueV3TokenCommand command = new IssueV3TokenCommand(this, tokenProviderDriver, userid, methodNames, expiresAt,
				projectid, domainid, authContext, trust, token, includeCatalog);
		return command.execute();
	}

	@Override
	public void checkV3Token(String tokenid) {
		CheckV3TokenCommand command = new CheckV3TokenCommand(this, tokenProviderDriver, tokenid);
		command.execute();
	}

	@Override
	public void revokeToken(String tokenid) {
		RevokeTokenCommand command = new RevokeTokenCommand(this, tokenProviderDriver, tokenid);
		command.execute();
	}

	@Override
	public TokenDataWrapper validateV3Token(String tokenid) {
		ValidateV3TokenCommand command = new ValidateV3TokenCommand(this, tokenProviderDriver, tokenid);
		return command.execute();
	}

	@Override
	public Entry<String, TokenV2DataWrapper> issueV2Token(Token authTokenData, List<Role> rolesRef, Catalog catalogRef) {
		IssueV2TokenCommand command = new IssueV2TokenCommand(this, tokenProviderDriver, authTokenData, rolesRef, catalogRef);
		return command.execute();
	}

	@Override
	public TokenV2DataWrapper validateV2Token(String tokenid, String belongsTo) {
		ValidateV2TokenCommand command = new ValidateV2TokenCommand(this, tokenProviderDriver, tokenid, belongsTo);
		return command.execute();
	}

	@Override
	public TokenV2DataWrapper checkV2Token(String tokenid, String belongsTo) {
		CheckV2TokenCommand command = new CheckV2TokenCommand(this, tokenProviderDriver, tokenid, belongsTo);
		return command.execute();
	}

	@Override
	public IToken validToken(String tokenid) {
		// TODO Auto-generated method stub
		return null;
	}
}
