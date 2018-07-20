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

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.token.model.KeystoneToken;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class RevokeTokenCommand extends AbstractTokenProviderCommand implements NonTruncatedCommand<TokenIdAndData> {

	private final static Logger logger = LoggerFactory.getLogger(RevokeTokenCommand.class);
	private final String tokenid;
	private final boolean revokeChain;


	public RevokeTokenCommand(TokenProviderApi tokenProviderApi, RevokeApi revokeApi,
			TokenProviderDriver tokenProviderDriver, PersistenceManager persistenceManager, String tokenid,
			boolean revokeChain) {
		super(tokenProviderApi, revokeApi, tokenProviderDriver, persistenceManager);
		this.tokenid = tokenid;
		this.revokeChain = revokeChain;
	}

	@Override
	public TokenIdAndData execute() throws Exception {
		if (this.getRevokeApi() != null) {
			boolean revokeByExpires = false;
			String projectId = null;
			String domainId = null;

			KeystoneToken tokenRef = new KeystoneToken(tokenid, validateToken(tokenid));

			String userId = tokenRef.getUserId();
			Calendar expiresAt = tokenRef.getExpires();
			String auditId = tokenRef.getAutitId();
			String auditChainId = tokenRef.getAutitChainId();
			if (tokenRef.isProjectScoped()) {
				projectId = tokenRef.getProjectId();
			}
			if (tokenRef.isDomainScoped()) {
				domainId = tokenRef.getDomainId();
			}

			if (Strings.isNullOrEmpty(auditId) && !revokeChain) {
				logger.debug("Received token with no audit_id");
				revokeByExpires = true;
			}

			if (Strings.isNullOrEmpty(auditChainId) && revokeChain) {
				logger.debug("Received token with no audit_Chain_id");
				revokeByExpires = true;
			}

			if (revokeByExpires) {
				this.getRevokeApi().revokeByExpiration(userId, expiresAt, projectId, domainId);
			} else if (revokeChain) {
				this.getRevokeApi().revokeByAuditChainId(auditChainId, projectId, domainId);
			} else {
				this.getRevokeApi().revokeByAuditId(auditId);
			}

		}

		if (Config.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
			this.getPersistence().deleteToken(tokenid);
		}
		return null;
	}
}
