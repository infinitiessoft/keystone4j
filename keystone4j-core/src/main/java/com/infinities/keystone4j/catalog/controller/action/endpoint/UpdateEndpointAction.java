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
package com.infinities.keystone4j.catalog.controller.action.endpoint;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateEndpointAction extends AbstractEndpointAction implements ProtectedAction<Endpoint> {

	private final static Logger logger = LoggerFactory.getLogger(UpdateEndpointAction.class);
	private final String endpointid;
	private Endpoint endpoint;


	public UpdateEndpointAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String endpointid, Endpoint endpoint) {
		super(catalogApi, tokenProviderApi, policyApi);
		this.endpointid = endpointid;
		this.endpoint = endpoint;
	}

	@Override
	public MemberWrapper<Endpoint> execute(ContainerRequestContext context) throws Exception {
		logger.debug("get endpoint: {}", endpointid);
		requireMatchingId(endpointid, endpoint);
		if (!Strings.isNullOrEmpty(endpoint.getServiceid())) {
			catalogApi.getService(endpoint.getServiceid());
		}
		endpoint = validateEndpointRegion(endpoint);
		Endpoint ref = this.getCatalogApi().updateEndpoint(endpointid, endpoint);
		return this.wrapMember(context, ref);
	}

	@Override
	public String getName() {
		return "update_endpoint";
	}
}
