package com.infinities.keystone4j.token.provider.api.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class ValidateV3TokenCommand extends AbstractTokenProviderCommand<TokenDataWrapper> {

	private final String tokenid;
	private final static Logger logger = LoggerFactory.getLogger(ValidateV3TokenCommand.class);


	// private final static String UNEXPECTED_ERROR =
	// "Unexpected error or malformed token determining token expiry: {}";
	// private final static String FAILED_TO_VALIDATE_TOKEN =
	// "Failed to validate token";

	public ValidateV3TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
	}

	@Override
	public TokenDataWrapper execute() {
		String uniqueid = this.getUniqueId(tokenid);
		TokenDataWrapper token = this.getTokenProviderDriver().validateV3Token(uniqueid);
		logger.debug("validate token uniqueid: {}", tokenid);
		isValidToken(token);
		return token;
	}
}
