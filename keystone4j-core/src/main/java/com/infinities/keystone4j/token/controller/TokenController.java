package com.infinities.keystone4j.token.controller;

import java.io.OutputStream;

import com.infinities.keystone4j.catalog.model.EndpointsWrapper;
import com.infinities.keystone4j.token.model.Auth;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.model.TokenWrapper;
import com.infinities.keystone4j.trust.model.SignedWrapper;

public interface TokenController {

	OutputStream getCaCert();

	OutputStream getSigningCert();

	TokenWrapper authenticate(Auth auth);

	Token validateTokenHead();

	Token validateToken();

	void deleteToken();

	SignedWrapper getRevocationList();

	EndpointsWrapper getEndpoints();

}
