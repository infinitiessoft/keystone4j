package com.infinities.keystone4j.token.provider.api.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class ValidateV3TokenCommand extends AbstractTokenProviderCommand implements NonTruncatedCommand<TokenDataWrapper> {

	private final String tokenid;
	private final static Logger logger = LoggerFactory.getLogger(ValidateV3TokenCommand.class);


	// private final static String UNEXPECTED_ERROR =
	// "Unexpected error or malformed token determining token expiry: {}";
	// private final static String FAILED_TO_VALIDATE_TOKEN =
	// "Failed to validate token";

	public ValidateV3TokenCommand(TokenProviderApi tokenProviderApi, RevokeApi revokeApi,
			TokenProviderDriver tokenProviderDriver, PersistenceManager persistenceManager, String tokenid) {
		super(tokenProviderApi, revokeApi, tokenProviderDriver, persistenceManager);
		this.tokenid = tokenid;
	}

	@Override
	public TokenDataWrapper execute() throws Exception {
		String uniqueId = this.getUniqueId(tokenid);
		Token tokenRef = null;
		try {
			tokenRef = this.getPersistence().getToken(uniqueId);
		} catch (Exception e) {
			logger.debug("get token failed", e);
			throw Exceptions.TokenNotFoundException.getInstance(null, tokenid);
		}
		logger.debug("validate token uniqueid: {}", tokenid);
		logger.debug("token exist?: {}", String.valueOf(tokenRef != null));
		TokenDataWrapper token = validateV3Token(tokenRef);
		isValidToken(token);
		return token;
	}

	private TokenDataWrapper validateV3Token(Token tokenRef) throws Exception {
		return this.getTokenProviderDriver().validateV3Token(tokenRef);
	}
}
