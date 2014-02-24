package com.infinities.keystone4j.token.provider.command;

import com.infinities.keystone4j.Cms;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class CheckV3TokenCommand extends AbstractTokenProviderCommand<TokenMetadata> {

	private final String tokenid;


	public CheckV3TokenCommand(TokenApi tokenApi, TokenProviderApi tokenProviderApi,
			TokenProviderDriver tokenProviderDriver, String tokenid) {
		super(tokenApi, tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
	}

	@Override
	public TokenMetadata execute() {
		String uniqueid = new Cms().uniqueid(tokenid);
		this.getTokenProviderApi().validateV3Token(uniqueid);
		return null;
	}

}
