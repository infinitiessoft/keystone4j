package com.infinities.keystone4j.token.provider.api.command;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.model.token.wrapper.ITokenDataWrapper;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class ValidateTokenCommand extends AbstractTokenProviderCommand implements NonTruncatedCommand<ITokenDataWrapper> {

	private final String tokenid;
	private final String belongsTo;


	// private final static Logger logger =
	// LoggerFactory.getLogger(ValidateTokenCommand.class);

	// private final static String UNEXPECTED_ERROR =
	// "Unexpected error or malformed token determining token expiry: {}";
	// private final static String FAILED_TO_VALIDATE_TOKEN =
	// "Failed to validate token";

	public ValidateTokenCommand(TokenProviderApi tokenProviderApi, RevokeApi revokeApi,
			TokenProviderDriver tokenProviderDriver, PersistenceManager persistenceManager, String tokenid, String belongsTo) {
		super(tokenProviderApi, revokeApi, tokenProviderDriver, persistenceManager);
		this.tokenid = tokenid;
		this.belongsTo = belongsTo;
	}

	@Override
	public ITokenDataWrapper execute() throws Exception {
		String uniqueId = getUniqueId(tokenid);
		ITokenDataWrapper token = validateToken(uniqueId);
		tokenBelongsTo(token, belongsTo);
		isValidToken(token);
		return token;
	}
}
