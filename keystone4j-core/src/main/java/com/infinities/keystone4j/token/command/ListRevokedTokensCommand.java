package com.infinities.keystone4j.token.command;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class ListRevokedTokensCommand extends AbstractTokenCommand<List<Token>> {

	public ListRevokedTokensCommand(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TrustApi trustApi, TokenApi tokenApi, TokenDriver tokenDriver) {
		super(assignmentApi, identityApi, tokenProviderApi, trustApi, tokenApi, tokenDriver);
	}

	@Override
	public List<Token> execute() {
		return this.getTokenDriver().listRevokeTokens();
	}

}
