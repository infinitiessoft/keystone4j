package com.infinities.keystone4j.token.api;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.command.CreateTokenCommand;
import com.infinities.keystone4j.token.command.DeleteTokenCommand;
import com.infinities.keystone4j.token.command.DeleteTokensForDomainCommand;
import com.infinities.keystone4j.token.command.DeleteTokensForTrustCommand;
import com.infinities.keystone4j.token.command.DeleteTokensForUserCommand;
import com.infinities.keystone4j.token.command.GetTokenCommand;
import com.infinities.keystone4j.token.command.ListRevokedTokensCommand;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class TokenApiImpl implements TokenApi {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final TrustApi trustApi;
	private final TokenDriver tokenDriver;


	public TokenApiImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TrustApi trustApi, TokenDriver tokenDriver) {
		super();
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.tokenDriver = tokenDriver;
	}

	@Override
	public Token getToken(String tokenid) {
		GetTokenCommand command = new GetTokenCommand(assignmentApi, identityApi, tokenProviderApi, trustApi, this,
				tokenDriver, tokenid);
		return command.execute();
	}

	@Override
	public List<Token> listRevokedTokens() {
		ListRevokedTokensCommand command = new ListRevokedTokensCommand(assignmentApi, identityApi, tokenProviderApi,
				trustApi, this, tokenDriver);
		return command.execute();
	}

	@Override
	public void deleteTokensForTrust(String userid, String trustid) {
		DeleteTokensForTrustCommand command = new DeleteTokensForTrustCommand(assignmentApi, identityApi, tokenProviderApi,
				trustApi, this, tokenDriver, userid, trustid);
		command.execute();
	}

	@Override
	public void deleteTokensForUser(String userid, String projectid) {
		DeleteTokensForUserCommand command = new DeleteTokensForUserCommand(assignmentApi, identityApi, tokenProviderApi,
				trustApi, this, tokenDriver, userid, projectid);
		command.execute();

	}

	@Override
	public void deleteTokensForDomain(String domainid) {
		DeleteTokensForDomainCommand command = new DeleteTokensForDomainCommand(assignmentApi, identityApi,
				tokenProviderApi, trustApi, this, tokenDriver, domainid);
		command.execute();
	}

	@Override
	public Token createToken(Token token) {
		CreateTokenCommand command = new CreateTokenCommand(assignmentApi, identityApi, tokenProviderApi, trustApi, this,
				tokenDriver, token);
		return command.execute();
	}

	@Override
	public void deleteToken(String tokenid) {
		DeleteTokenCommand command = new DeleteTokenCommand(assignmentApi, identityApi, tokenProviderApi, trustApi, this,
				tokenDriver, tokenid);
		command.execute();
	}

}
