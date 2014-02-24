package com.infinities.keystone4j.token.provider.command;

import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class RevokeTokenCommand extends AbstractTokenProviderCommand<TokenMetadata> {

	private final String tokenid;


	public RevokeTokenCommand(TokenApi tokenApi, TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver,
			String tokenid) {
		super(tokenApi, tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
	}

	@Override
	public TokenMetadata execute() {
		this.getTokenProviderDriver().revokeToken(tokenid);
		return null;
	}

}
