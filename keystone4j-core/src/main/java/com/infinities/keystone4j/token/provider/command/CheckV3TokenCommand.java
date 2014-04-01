package com.infinities.keystone4j.token.provider.command;

import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.utils.Cms;

public class CheckV3TokenCommand extends AbstractTokenProviderCommand<TokenMetadata> {

	private final String tokenid;


	public CheckV3TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
	}

	@Override
	public TokenMetadata execute() {
		String uniqueid = Cms.Instance.hashToken(tokenid);
		this.getTokenProviderApi().validateV3Token(uniqueid);
		return null;
	}

}
