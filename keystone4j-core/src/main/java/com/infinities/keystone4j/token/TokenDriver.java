package com.infinities.keystone4j.token;

import java.util.List;

import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.token.provider.api.command.AbstractTokenProviderCommand.Data;

public interface TokenDriver {

	Token getToken(String uniqueid) throws Exception;

	Token createToken(String uniqueid, Data data) throws Exception;

	void deleteToken(String tokenid) throws Exception;

	// void deleteTokens();

	// List<Token> listTokens();

	List<Token> listRevokeTokens();

	void flushExpiredTokens();

	// void deleteTokensForTrust(String userid, String trustid);

	// void deleteTokensForUser(String userid, String projectid);

	List<String> listTokens(String userId, String tenantId, String trustId, String consumerId);

	void deleteTokens(String userId, String tenantId, String trustId, String consumerId);

}
