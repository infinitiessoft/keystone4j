package com.infinities.keystone4j.token.controller;

import javax.ws.rs.core.Response;

import com.infinities.keystone4j.model.token.Auth;
import com.infinities.keystone4j.model.token.v2.EndpointsV2Wrapper;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.model.trust.SignedWrapper;

public interface TokenController {

	Response getCaCert();

	Response getSigningCert();

	TokenV2DataWrapper authenticate(Auth auth);

	void validateTokenHead(String tokenid, String belongsTo);

	TokenV2DataWrapper validateToken(String tokenid, String belongsTo);

	void deleteToken(String tokenid);

	SignedWrapper getRevocationList();

	EndpointsV2Wrapper getEndpoints(String tokenid);

}
