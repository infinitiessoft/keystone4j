/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
