package com.infinities.keystone4j.auth.controller;

import com.infinities.keystone4j.auth.model.AuthV3;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.trust.model.SignedWrapper;

public interface AuthController {

	public final static String NOCATALOG = "nocatalog";


	TokenMetadata authenticateForToken(AuthV3 auth);

	void checkToken();

	void revokeToken();

	TokenMetadata validateToken();

	SignedWrapper getRevocationList();

	// keystone.common.cms.sign_token
	// String getTokenId(Token token);

}
