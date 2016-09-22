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
		logger.debug("validate token uniqueid: {}", tokenid);
		Token tokenRef = this.getPersistence().getToken(uniqueId);
		TokenV2DataWrapper token = validateV2Token(tokenRef);
		checkRevocationV2(token);
		tokenBelongsTo(token, belongsTo);
		isValidToken(token);
		return token;
	}

	private TokenV2DataWrapper validateV2Token(Token tokenRef) throws Exception {
		return this.getTokenProviderDriver().validateV2Token(tokenRef);
	}
}
