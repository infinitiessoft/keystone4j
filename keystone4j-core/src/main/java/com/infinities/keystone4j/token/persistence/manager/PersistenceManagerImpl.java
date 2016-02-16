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
package com.infinities.keystone4j.token.persistence.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.common.base.Strings;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.api.command.AbstractTokenProviderCommand.Data;
import com.infinities.keystone4j.trust.TrustApi;

public class PersistenceManagerImpl implements PersistenceManager {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private TokenProviderApi tokenProviderApi;
	private final TrustApi trustApi;
	private final TokenDriver tokenDriver;


	public PersistenceManagerImpl(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenDriver tokenDriver) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.tokenDriver = tokenDriver;
		this.assignmentApi.setIdentityApi(identityApi);
	}

	@Override
	public void setTokenProviderApi(TokenProviderApi tokenProviderApi) {
		this.tokenProviderApi = tokenProviderApi;
	}

	private void assertValid(String tokenId, Token tokenRef) {
		Calendar currentTime = Calendar.getInstance();
		Calendar expires = tokenRef.getExpires();
		if (expires == null || currentTime.after(expires)) {
			throw Exceptions.TokenNotFoundException.getInstance(null, tokenId);
		}
	}

	@Override
	public Token getToken(String tokenId) throws Exception {
		if (Strings.isNullOrEmpty(tokenId)) {
			throw Exceptions.TokenNotFoundException.getInstance(null, "");
		}
		String uniqueId = tokenProviderApi.getUniqueId(tokenId);
		Token tokenRef = _getToken(uniqueId);
		assertValid(tokenId, tokenRef);
		return tokenRef;
	}

	private Token _getToken(String tokenId) throws Exception {
		return tokenDriver.getToken(tokenId);
	}

	@Override
	public Token createToken(String tokenId, Data data) throws Exception {
		String uniqueId = tokenProviderApi.getUniqueId(tokenId);
		data.setId(uniqueId);

		Token ret = tokenDriver.createToken(uniqueId, data);
		return ret;
	}

	@Override
	public void deleteToken(String tokenId) throws Exception {
		if (!Config.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
			return;
		}
		String uniqueId = tokenProviderApi.getUniqueId(tokenId);
		tokenDriver.deleteToken(uniqueId);
		// TODO ignore self._invalidate_individual_token_cache(unique_id)
		// TODO ignore self.invalidate_revocation_list()
	}

	// tenantId = null, trustId = null, consumerId = null
	@Override
	public void deleteTokens(String userId, String tenantId, String trustId, String consumerId) {
		if (!Config.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
			return;
		}
		// List<String> tokenList = tokenDriver.listTokens(userId, tenantId,
		// trustId, consumerId);

		tokenDriver.deleteTokens(userId, tenantId, trustId, consumerId);
		// for (String tokenId : tokenList) {
		// String uniqueId = tokenProviderApi.getUniqueId(tokenId);
		// ignore self._invalidate_individual_token_cache(unique_id)
		// }
		// TODO ignore self.invalidate_revocation_list()
	}

	@Override
	public List<Token> listRevokedTokens() {
		return tokenDriver.listRevokeTokens();
	}

	@Override
	public void deleteTokensForDomain(String domainId) throws Exception {
		if (!Config.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
			return;
		}
		List<Project> projects = assignmentApi.listProjects(null);
		for (Project project : projects) {
			if (domainId.equals(project.getDomainId())) {
				for (String userId : assignmentApi.listUserIdsForProject(project.getId())) {
					deleteTokensForUser(userId, project.getId());
				}
			}
		}

		List<User> users = identityApi.listUsers(domainId, null);
		List<String> userIds = new ArrayList<String>();
		for (User user : users) {
			userIds.add(user.getId());
		}
		deleteTokensForUsers(userIds, null);
	}

	// projectId = null
	@Override
	public void deleteTokensForUser(String userId, String projectId) {
		if (!Config.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
			return;
		}
		deleteTokens(userId, projectId, null, null);
		for (Trust trust : trustApi.listTrustsForTrustee(userId)) {
			deleteTokens(userId, projectId, trust.getId(), null);
		}
		for (Trust trust : trustApi.listTrustsForTrustor(userId)) {
			deleteTokens(trust.getTrusteeUserId(), projectId, trust.getId(), null);
		}
	}

	// projectId = null
	@Override
	public void deleteTokensForUsers(List<String> userIds, String projectId) {
		if (!Config.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
			return;
		}
		for (String userId : userIds) {
			deleteTokensForUser(userId, projectId);
		}
	}
}
