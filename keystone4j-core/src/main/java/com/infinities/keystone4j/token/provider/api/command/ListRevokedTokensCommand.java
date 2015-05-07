/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
