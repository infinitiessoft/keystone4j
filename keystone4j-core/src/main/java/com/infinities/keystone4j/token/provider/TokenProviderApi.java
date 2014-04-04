package com.infinities.keystone4j.token.provider;

import java.util.Date;
import java.util.List;

import com.infinities.keystone4j.Api;
import com.infinities.keystone4j.auth.model.AuthContext;
import com.infinities.keystone4j.model.auth.TokenMetadata;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.trust.Trust;

public interface TokenProviderApi extends Api {

	TokenMetadata issueV3Token(String userid, List<String> methodNames, Date expiresAt, String projectid, String domainid,
			AuthContext authContext, Trust trust, Token token, boolean includeCatalog);

	void checkV3Token(String tokenid);

	void revokeToken(String tokenid);

	TokenDataWrapper validateV3Token(String tokenid);

}
