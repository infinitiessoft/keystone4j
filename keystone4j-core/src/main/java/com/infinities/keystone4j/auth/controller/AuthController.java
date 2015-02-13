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
