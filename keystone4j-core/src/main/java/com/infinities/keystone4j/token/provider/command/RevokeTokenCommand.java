package com.infinities.keystone4j.token.provider.command;

import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class RevokeTokenCommand extends AbstractTokenProviderCommand<TokenMetadata> {

	private final String tokenid;


	public RevokeTokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
	}

	@Override
	public TokenMetadata execute() {
		this.getTokenProviderDriver().revokeToken(tokenid);
		return null;
	}

}
