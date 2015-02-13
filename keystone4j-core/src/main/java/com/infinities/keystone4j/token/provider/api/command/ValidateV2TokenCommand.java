package com.infinities.keystone4j.token.provider.api.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class ValidateV2TokenCommand extends AbstractTokenProviderCommand implements NonTruncatedCommand<TokenV2DataWrapper> {

	private final String tokenid;
	private final String belongsTo;
	private final static Logger logger = LoggerFactory.getLogger(ValidateV2TokenCommand.class);


	public ValidateV2TokenCommand(TokenProviderApi tokenProviderApi, RevokeApi revokeApi,
			TokenProviderDriver tokenProviderDriver, PersistenceManager persistenceManager, String tokenid, String belongsTo) {
		super(tokenProviderApi, revokeApi, tokenProviderDriver, persistenceManager);
		this.tokenid = tokenid;
		this.belongsTo = belongsTo;
	}

	@Override
	public TokenV2DataWrapper execute() throws Exception {
		String uniqueId = getUniqueId(tokenid);
		Token tokenRef = this.getPersistence().getToken(uniqueId);
		TokenV2DataWrapper token = validateV2Token(tokenRef);
		checkRevocationV2(token);
		tokenBelongsTo(token, belongsTo);
		logger.debug("validate token uniqueid: {}", tokenid);
		isValidToken(token);
		return token;
	}

	private TokenV2DataWrapper validateV2Token(Token tokenRef) throws Exception {
		return this.getTokenProviderDriver().validateV2Token(tokenRef);
	}
}
