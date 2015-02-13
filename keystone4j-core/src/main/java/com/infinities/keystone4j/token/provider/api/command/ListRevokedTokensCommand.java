package com.infinities.keystone4j.token.provider.api.command;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class ListRevokedTokensCommand extends AbstractTokenProviderCommand implements NonTruncatedCommand<List<Token>> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(CheckV2TokenCommand.class);

	public ListRevokedTokensCommand(TokenProviderApi tokenProviderApi, RevokeApi revokeApi,
			TokenProviderDriver tokenProviderDriver, PersistenceManager persistenceManager) {
		super(tokenProviderApi, revokeApi, tokenProviderDriver, persistenceManager);

	}

	@Override
	public List<Token> execute() throws Exception {
		return this.getPersistence().listRevokedTokens();
	}

}
