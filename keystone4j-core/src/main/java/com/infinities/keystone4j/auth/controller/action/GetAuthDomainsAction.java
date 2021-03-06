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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.action.domain.AbstractDomainAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Authorization;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetAuthDomainsAction extends AbstractAuthAction implements FilterProtectedAction<Domain> {

	private final static Logger logger = LoggerFactory.getLogger(GetAuthDomainsAction.class);


	public GetAuthDomainsAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Domain> execute(ContainerRequestContext context, String... filters) throws Exception {
		Authorization.AuthContext authContext = AbstractAction.getAuthContext(context);
		String userid = authContext.getUserId();
		List<Domain> userRefs = new ArrayList<Domain>();
		if (!Strings.isNullOrEmpty(userid)) {
			try {
				userRefs = this.assignmentApi.listDomainsForUser(userid, null);
			} catch (Exception e) {
				logger.debug("ignore", e);
			}
		}

		List<String> groupids = authContext.getGroupIds();
		List<Domain> groupRefs = new ArrayList<Domain>();
		if (groupids != null && !groupids.isEmpty()) {
			groupRefs = this.assignmentApi.listDomainsForGroups(groupids);
		}

		List<Domain> refs = combineListsUniquely(userRefs, groupRefs);

		return new AbstractDomainAction(assignmentApi, tokenProviderApi, policyApi) {

			@Override
			public String getName() {
				return null;
			}

		}.wrapCollection(context, refs);
	}

	private List<Domain> combineListsUniquely(List<Domain> a, List<Domain> b) {
		if (a != null && b != null) {
			Map<String, Domain> map = new HashMap<String, Domain>();
			for (Domain p : a) {
				map.put(p.getId(), p);
			}
			for (Domain p : b) {
				map.put(p.getId(), p);
			}
			return Lists.newArrayList(map.values());
		} else if (a != null) {
			return a;
		} else {
			return b;
		}
	}

	@Override
	public String getName() {
		return "get_auth_projects";
	}

}
