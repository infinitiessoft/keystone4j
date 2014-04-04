package com.infinities.keystone4j.auth.controller;

import com.infinities.keystone4j.model.auth.AuthV3;
import com.infinities.keystone4j.model.auth.TokenMetadata;
import com.infinities.keystone4j.model.trust.SignedWrapper;

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
