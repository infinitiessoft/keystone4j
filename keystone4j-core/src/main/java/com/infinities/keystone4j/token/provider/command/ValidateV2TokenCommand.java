package com.infinities.keystone4j.token.provider.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class ValidateV2TokenCommand extends AbstractTokenProviderCommand<TokenV2DataWrapper> {

	private final String tokenid;
	private final String belongsTo;
	private final static Logger logger = LoggerFactory.getLogger(ValidateV2TokenCommand.class);


	public ValidateV2TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver,
			String tokenid, String belongsTo) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
		this.belongsTo = belongsTo;
	}

	@Override
	public TokenV2DataWrapper execute() {
		String uniqueId = this.getUniqueId(tokenid);
		TokenV2DataWrapper token = this.getTokenProviderDriver().validateV2Token(uniqueId);
		tokenBelongsTo(token, belongsTo);
		logger.debug("validate token uniqueid: {}", tokenid);
		isValidToken(token);
		return token;
	}

}
