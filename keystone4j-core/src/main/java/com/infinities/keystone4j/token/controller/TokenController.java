package com.infinities.keystone4j.token.controller;

import java.io.OutputStream;

import com.infinities.keystone4j.model.catalog.EndpointsWrapper;
import com.infinities.keystone4j.model.token.Auth;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.TokenWrapper;
import com.infinities.keystone4j.model.trust.SignedWrapper;

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
