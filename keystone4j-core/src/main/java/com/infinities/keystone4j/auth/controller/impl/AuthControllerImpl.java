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
package com.infinities.keystone4j.auth.controller.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.auth.controller.action.AuthenticationForTokenAction;
import com.infinities.keystone4j.auth.controller.action.CheckTokenAction;
import com.infinities.keystone4j.auth.controller.action.GetAuthCatalogAction;
import com.infinities.keystone4j.auth.controller.action.GetAuthDomainsAction;
import com.infinities.keystone4j.auth.controller.action.GetAuthProjectsAction;
import com.infinities.keystone4j.auth.controller.action.GetRevocationListAction;
import com.infinities.keystone4j.auth.controller.action.RevokeTokenAction;
import com.infinities.keystone4j.auth.controller.action.ValidateTokenAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.controller.action.decorator.ProtectedCollectionDecorator;
import com.infinities.keystone4j.controller.action.decorator.ProtectedDecorator;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.auth.AuthV3;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

//keystone.auth.controllers 20141210

public class AuthControllerImpl extends BaseController implements AuthController {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AuthControllerImpl.class);
	private final static String SUBJECT_TOKEN_HEADER = "X-Subject-Token";
	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final TokenProviderApi tokenProviderApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final PolicyApi policyApi;


	public AuthControllerImpl(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.trustApi = trustApi;
		this.policyApi = policyApi;
		this.assignmentApi.setIdentityApi(identityApi);
		this.identityApi.setAssignmentApi(assignmentApi);
	}

	@Override
	public Response authenticateForToken(AuthV3 auth) throws Exception {
		AuthenticationForTokenAction command = new AuthenticationForTokenAction(assignmentApi, catalogApi, identityApi,
				tokenProviderApi, trustApi, policyApi, auth);
		TokenIdAndData res = command.execute(getRequest());
		return renderTokenDataResponse(res.getTokenid(), res.getTokenData(), true);
	}

	@Override
	public Response checkToken() throws Exception {
		ProtectedAction<TokenDataWrapper> command = new ProtectedDecorator<TokenDataWrapper>(new CheckTokenAction(
				assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		TokenIdAndData res = (TokenIdAndData) command.execute(getRequest());
		return renderTokenDataResponse(res.getTokenid(), res.getTokenData());
	}

	@Override
	public void revokeToken() throws Exception {
		ProtectedAction<TokenDataWrapper> command = new ProtectedDecorator<TokenDataWrapper>(new RevokeTokenAction(
				assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		command.execute(getRequest());
	}

	@Override
	public Response validateToken() throws Exception {
		ProtectedAction<TokenDataWrapper> command = new ProtectedDecorator<TokenDataWrapper>(new ValidateTokenAction(
				assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		TokenIdAndData res = (TokenIdAndData) command.execute(getRequest());
		return renderTokenDataResponse(res.getTokenid(), res.getTokenData());
	}

	@Override
	public MemberWrapper<String> getRevocationList() throws Exception {
		ProtectedAction<String> command = new ProtectedDecorator<String>(new GetRevocationListAction(assignmentApi,
				catalogApi, identityApi, tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		return command.execute(getRequest());
	}

	protected Response renderTokenDataResponse(String tokenid, TokenDataWrapper tokenData) {
		return renderTokenDataResponse(tokenid, tokenData, false);
	}

	protected Response renderTokenDataResponse(String tokenid, TokenDataWrapper tokenData, boolean created) {
		if (created) {
			return Response.status(Status.CREATED).entity(tokenData).header(SUBJECT_TOKEN_HEADER, tokenid).build();
		} else {
			return Response.status(Status.OK).entity(tokenData).header(SUBJECT_TOKEN_HEADER, tokenid).build();
		}
	}

	@Override
	public CollectionWrapper<Service> getAuthCatalog() throws Exception {
		FilterProtectedAction<Service> command = new ProtectedCollectionDecorator<Service>(new GetAuthCatalogAction(
				assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		return command.execute(getRequest());
	}

	@Override
	public CollectionWrapper<Project> getAuthProjects() throws Exception {
		FilterProtectedAction<Project> command = new ProtectedCollectionDecorator<Project>(new GetAuthProjectsAction(
				assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		return command.execute(getRequest());
	}

	@Override
	public CollectionWrapper<Domain> getAuthDomains() throws Exception {
		FilterProtectedAction<Domain> command = new ProtectedCollectionDecorator<Domain>(new GetAuthDomainsAction(
				assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		return command.execute(getRequest());
	}
}
