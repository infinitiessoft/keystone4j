package com.infinities.keystone4j.token.command;

import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.trust.TrustApi;

public class DeleteTokenCommand extends AbstractTokenCommand<Token> {

	private final String tokenid;


	public DeleteTokenCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, String tokenid) {
		super(tokenApi, trustApi, tokenDriver);
		this.tokenid = tokenid;
	}

	@Override
	public Token execute() {
		this.getTokenDriver().deleteToken(tokenid);
		return null;
	}

}
