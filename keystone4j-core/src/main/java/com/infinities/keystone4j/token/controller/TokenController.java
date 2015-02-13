package com.infinities.keystone4j.token.controller;

import javax.ws.rs.core.Response;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.token.Auth;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.Access.Service;

public interface TokenController {

	Response getCaCert() throws Exception;

	Response getSigningCert() throws Exception;

	MemberWrapper<Access> authenticate(Auth auth) throws Exception;

	void validateTokenHead(String tokenid) throws Exception;

	MemberWrapper<Access> validateToken(String tokenid) throws Exception;

	void deleteToken(String tokenid) throws Exception;

	MemberWrapper<String> getRevocationList() throws Exception;

	Service getEndpoints(String tokenid) throws Exception;

}
