package com.infinities.keystone4j.token.command;

import java.util.Date;

import com.google.common.base.Strings;
import com.infinities.keystone4j.Cms;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.trust.TrustApi;

public class GetTokenCommand extends AbstractTokenCommand<Token> {

	private final String tokenid;


	public GetTokenCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, String tokenid) {
		super(tokenApi, trustApi, tokenDriver);
		this.tokenid = tokenid;
	}

	@Override
	public Token execute() {
		if (Strings.isNullOrEmpty(tokenid)) {
			throw Exceptions.TokenNotFoundException.getInstance(null);
		}
		String uniqueid = Cms.Instance.hashToken(tokenid);
		Token token = this.getTokenDriver().getToken(uniqueid);
		assertValid(token);
		return token;
	}

	private void assertValid(Token token) {
		Date currentTime = new Date();
		Date expires = token.getExpires();

		if (expires == null || currentTime.after(expires)) {
			throw Exceptions.TokenNotFoundException.getInstance(null, token.getId());
		}

	}
}
