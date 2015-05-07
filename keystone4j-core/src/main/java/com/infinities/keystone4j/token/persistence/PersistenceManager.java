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
package com.infinities.keystone4j.token.persistence;

import java.util.List;

import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.api.command.AbstractTokenProviderCommand.Data;

public interface PersistenceManager {

	void setTokenProviderApi(TokenProviderApi tokenProviderApi) throws Exception;

	Token getToken(String tokenid) throws Exception;

	Token createToken(String tokenid, Data tokenData) throws Exception;

	List<Token> listRevokedTokens() throws Exception;

	void deleteToken(String tokenid) throws Exception;

	void deleteTokens(String userId, String tenantId, String trustId, String consumerId) throws Exception;

	// projectId=null
	void deleteTokensForUser(String userId, String projectId) throws Exception;

	void deleteTokensForDomain(String domainId) throws Exception;

	void deleteTokensForUsers(List<String> userIds, String projectId) throws Exception;

}
