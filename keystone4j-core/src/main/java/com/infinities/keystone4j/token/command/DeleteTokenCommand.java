package com.infinities.keystone4j.token.command;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class DeleteTokenCommand extends AbstractTokenCommand<Token> {

	private final String tokenid;


	public DeleteTokenCommand(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TrustApi trustApi, TokenApi tokenApi, TokenDriver tokenDriver, String tokenid) {
		super(assignmentApi, identityApi, tokenProviderApi, trustApi, tokenApi, tokenDriver);
		this.tokenid = tokenid;
	}

	@Override
	public Token execute() {
		this.getTokenDriver().deleteToken(tokenid);
		return null;
	}

}
