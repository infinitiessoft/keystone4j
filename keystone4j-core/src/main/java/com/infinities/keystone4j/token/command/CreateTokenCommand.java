package com.infinities.keystone4j.token.command;

import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.trust.TrustApi;

public class CreateTokenCommand extends AbstractTokenCommand<Token> {

	private final Token token;


	public CreateTokenCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, Token token) {
		super(tokenApi, trustApi, tokenDriver);
		this.token = token;
	}

	@Override
	public Token execute() {
		token.setValid(true);
		Token ret = this.getTokenDriver().createToken(token);
		return ret;
	}

}
