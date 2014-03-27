package com.infinities.keystone4j.token;

import java.util.List;

import com.infinities.keystone4j.token.model.Token;

public interface TokenDriver {

	Token getToken(String uniqueid);

	Token createToken(Token token);

	void deleteToken(String tokenid);

	// void deleteTokens();

	// List<Token> listTokens();

	List<Token> listRevokeTokens();

	void flushExpiredTokens();

	void deleteTokensForTrust(String userid, String trustid);

	void deleteTokensForUser(String userid, String projectid);

}
