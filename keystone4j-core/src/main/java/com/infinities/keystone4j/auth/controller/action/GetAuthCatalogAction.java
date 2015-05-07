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
package com.infinities.keystone4j.auth.controller.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.google.common.base.Strings;
import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.model.catalog.wrapper.CatalogWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetAuthCatalogAction extends AbstractAuthAction implements FilterProtectedAction<Service> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(GetAuthCatalogAction.class);

	public GetAuthCatalogAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Service> execute(ContainerRequestContext context, String... filters) throws Exception {
		Authorization.AuthContext authContext = AbstractAction.getAuthContext(context);
		String userid = authContext.getUserId();
		String projectid = authContext.getProjectId();

		if (Strings.isNullOrEmpty(projectid)) {
			throw Exceptions.ForbiddenException
					.getInstance("A project-scoped token is required to produce a service  catalog.");
		}

		List<Service> services = catalogApi.getV3Catalog(userid, projectid);
		CatalogWrapper wrapper = new CatalogWrapper(services);
		wrapper.getLinks().setSelf(getBaseUrl(context, "auth/catalog"));
		return wrapper;
	}

	@Override
	public String getName() {
		return "get_auth_projects";
	}

}
