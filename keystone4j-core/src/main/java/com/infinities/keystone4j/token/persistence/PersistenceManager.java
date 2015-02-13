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
