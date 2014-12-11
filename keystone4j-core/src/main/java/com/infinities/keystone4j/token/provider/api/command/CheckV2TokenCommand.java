package com.infinities.keystone4j.token.provider.api.command;

import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class CheckV2TokenCommand extends AbstractTokenProviderCommand<TokenV2DataWrapper> {

	private final String tokenid;
	private final String belongsTo;


	// private final static Logger logger =
	// LoggerFactory.getLogger(CheckV2TokenCommand.class);

	public CheckV2TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid,
			String belongsTo) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
		this.belongsTo = belongsTo;
	}

	@Override
	public TokenV2DataWrapper execute() {
		return this.getTokenProviderApi().validateV2Token(tokenid, belongsTo);
	}

}
