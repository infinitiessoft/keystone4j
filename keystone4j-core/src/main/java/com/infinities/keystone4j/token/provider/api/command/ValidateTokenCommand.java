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
