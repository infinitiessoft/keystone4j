package com.infinities.keystone4j.token.command;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class CreateTokenCommand extends AbstractTokenCommand<Token> {

	private final Token token;


	public CreateTokenCommand(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TrustApi trustApi, TokenApi tokenApi, TokenDriver tokenDriver, Token token) {
		super(assignmentApi, identityApi, tokenProviderApi, trustApi, tokenApi, tokenDriver);
		this.token = token;
	}

	@Override
	public Token execute() {
		token.setValid(true);
		Token ret = this.getTokenDriver().createToken(token);
		return ret;
	}

}
