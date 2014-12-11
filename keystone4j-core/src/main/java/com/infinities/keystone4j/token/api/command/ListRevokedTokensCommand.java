package com.infinities.keystone4j.token.api.command;

import java.util.List;

import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.trust.TrustApi;

public class ListRevokedTokensCommand extends AbstractTokenCommand<List<Token>> {

	public ListRevokedTokensCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver) {
		super(tokenApi, trustApi, tokenDriver);
	}

	@Override
	public List<Token> execute() {
		return this.getTokenDriver().listRevokeTokens();
	}

}
