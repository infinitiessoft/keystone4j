package com.infinities.keystone4j.token.provider.api.command;

import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class RevokeTokenCommand extends AbstractTokenProviderCommand<TokenIdAndData> {

	private final String tokenid;


	public RevokeTokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
	}

	@Override
	public TokenIdAndData execute() {
		this.getTokenProviderDriver().revokeToken(tokenid);
		return null;
	}

}
