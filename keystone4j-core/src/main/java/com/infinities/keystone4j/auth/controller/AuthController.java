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
package com.infinities.keystone4j.auth.controller;

import javax.ws.rs.core.Response;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.auth.AuthV3;
import com.infinities.keystone4j.model.catalog.Service;

public interface AuthController {

	public final static String NOCATALOG = "nocatalog";


	Response authenticateForToken(AuthV3 auth) throws Exception;

	Response checkToken() throws Exception;

	void revokeToken() throws Exception;

	Response validateToken() throws Exception;

	MemberWrapper<String> getRevocationList() throws Exception;

	CollectionWrapper<Service> getAuthCatalog() throws Exception;

	CollectionWrapper<Project> getAuthProjects() throws Exception;

	CollectionWrapper<Domain> getAuthDomains() throws Exception;

	// keystone.common.cms.sign_token
	// String getTokenId(Token token);

}
