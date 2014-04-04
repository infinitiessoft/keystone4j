package com.infinities.keystone4j.token.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.command.CreateTokenCommand;
import com.infinities.keystone4j.token.command.DeleteTokenCommand;
import com.infinities.keystone4j.token.command.DeleteTokensForTrustCommand;
import com.infinities.keystone4j.token.command.DeleteTokensForUserCommand;
import com.infinities.keystone4j.token.command.GetTokenCommand;
import com.infinities.keystone4j.token.command.ListRevokedTokensCommand;
import com.infinities.keystone4j.trust.TrustApi;

public class TokenApiImpl implements TokenApi {

	private final TrustApi trustApi;
	private final TokenDriver tokenDriver;
	private final static Logger logger = LoggerFactory.getLogger(TokenApiImpl.class);


	public TokenApiImpl(TrustApi trustApi, TokenDriver tokenDriver) {
		super();
		this.trustApi = trustApi;
		this.tokenDriver = tokenDriver;
	}

	@Override
	public Token getToken(String tokenid) {
		GetTokenCommand command = new GetTokenCommand(this, trustApi, tokenDriver, tokenid);
		return command.execute();
	}

	@Override
	public List<Token> listRevokedTokens() {
		ListRevokedTokensCommand command = new ListRevokedTokensCommand(this, trustApi, tokenDriver);
		return command.execute();
	}

	@Override
	public void deleteTokensForTrust(String userid, String trustid) {
		DeleteTokensForTrustCommand command = new DeleteTokensForTrustCommand(this, trustApi, tokenDriver, userid, trustid);
		command.execute();
	}

	@Override
	public void deleteTokensForUser(String userid, String projectid) {
		DeleteTokensForUserCommand command = new DeleteTokensForUserCommand(this, trustApi, tokenDriver, userid, projectid);
		command.execute();

	}

	// @Override
	// public void deleteTokensForDomain(String domainid) {
	// DeleteTokensForDomainCommand command = new
	// DeleteTokensForDomainCommand(domainid);
	// command.execute();
	// }

	@Override
	public Token createToken(Token token) {
		logger.debug("create token");
		CreateTokenCommand command = new CreateTokenCommand(this, trustApi, tokenDriver, token);
		return command.execute();
	}

	@Override
	public void deleteToken(String tokenid) {
		DeleteTokenCommand command = new DeleteTokenCommand(this, trustApi, tokenDriver, tokenid);
		command.execute();
	}

}
