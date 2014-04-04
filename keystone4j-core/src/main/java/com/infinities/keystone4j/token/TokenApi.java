package com.infinities.keystone4j.token;

import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.model.token.Token;

public interface TokenApi extends Api {

	Token getToken(String tokenid);

	List<Token> listRevokedTokens();

	void deleteTokensForTrust(String userid, String trustid);

	void deleteTokensForUser(String userid, String projectid);

	// void deleteTokensForDomain(String domainid);

	Token createToken(Token token);

	void deleteToken(String tokenid);
}
