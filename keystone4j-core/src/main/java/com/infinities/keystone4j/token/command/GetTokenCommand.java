package com.infinities.keystone4j.token.command;

import java.util.Date;

import com.google.common.base.Strings;
import com.infinities.keystone4j.Cms;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class GetTokenCommand extends AbstractTokenCommand<Token> {

	private final String tokenid;


	public GetTokenCommand(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TrustApi trustApi, TokenApi tokenApi, TokenDriver tokenDriver, String tokenid) {
		super(assignmentApi, identityApi, tokenProviderApi, trustApi, tokenApi, tokenDriver);
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
