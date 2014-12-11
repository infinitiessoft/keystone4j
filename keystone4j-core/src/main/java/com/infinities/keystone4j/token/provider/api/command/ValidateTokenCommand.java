package com.infinities.keystone4j.token.provider.api.command;

import com.infinities.keystone4j.model.token.IToken;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class ValidateTokenCommand extends AbstractTokenProviderCommand<IToken> {

	private final String tokenid;
	private final String belongsTo;


	// private final static Logger logger =
	// LoggerFactory.getLogger(ValidateTokenCommand.class);

	// private final static String UNEXPECTED_ERROR =
	// "Unexpected error or malformed token determining token expiry: {}";
	// private final static String FAILED_TO_VALIDATE_TOKEN =
	// "Failed to validate token";

	public ValidateTokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid,
			String belongsTo) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
		this.belongsTo = belongsTo;
	}

	@Override
	public IToken execute() {
		String uniqueid = this.getUniqueId(tokenid);
		IToken token = this.validateToken(uniqueid);
		tokenBelongsTo(token, belongsTo);
		isValidToken(token);
		return token;
	}
}
