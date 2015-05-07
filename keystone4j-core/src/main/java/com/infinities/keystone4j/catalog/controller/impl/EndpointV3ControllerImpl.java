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
package com.infinities.keystone4j.catalog.controller.impl;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.catalog.controller.action.endpoint.CreateEndpointAction;
import com.infinities.keystone4j.catalog.controller.action.endpoint.DeleteEndpointAction;
import com.infinities.keystone4j.catalog.controller.action.endpoint.GetEndpointAction;
import com.infinities.keystone4j.catalog.controller.action.endpoint.ListEndpointsAction;
import com.infinities.keystone4j.catalog.controller.action.endpoint.UpdateEndpointAction;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.controller.action.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.controller.action.decorator.ProtectedDecorator;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.catalog.controllers 20141216

public class EndpointV3ControllerImpl extends BaseController implements EndpointV3Controller {

	private final CatalogApi catalogApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	public EndpointV3ControllerImpl(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public MemberWrapper<Endpoint> createEndpoint(Endpoint endpoint) throws Exception {
		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new CreateEndpointAction(catalogApi,
				tokenProviderApi, policyApi, endpoint), tokenProviderApi, policyApi, null, endpoint);
		MemberWrapper<Endpoint> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public CollectionWrapper<Endpoint> listEndpoints() throws Exception {
		FilterProtectedAction<Endpoint> command = new FilterProtectedDecorator<Endpoint>(new ListEndpointsAction(catalogApi,
				tokenProviderApi, policyApi), tokenProviderApi, policyApi);
		CollectionWrapper<Endpoint> ret = command.execute(getRequest(), "interface", "service_id");
		return ret;
	}

	@Override
	public MemberWrapper<Endpoint> getEndpoint(String endpointid) throws Exception {
		Endpoint ref = getMemberFromDriver(endpointid);
		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new GetEndpointAction(catalogApi,
				tokenProviderApi, policyApi, endpointid), tokenProviderApi, policyApi, ref, null);
		MemberWrapper<Endpoint> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public MemberWrapper<Endpoint> updateEndpoint(String endpointid, Endpoint endpoint) throws Exception {
		Endpoint ref = getMemberFromDriver(endpointid);
		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new UpdateEndpointAction(catalogApi,
				tokenProviderApi, policyApi, endpointid, endpoint), tokenProviderApi, policyApi, ref, endpoint);
		MemberWrapper<Endpoint> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteEndpoint(String endpointid) throws Exception {
		Endpoint ref = getMemberFromDriver(endpointid);
		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new DeleteEndpointAction(catalogApi,
				tokenProviderApi, policyApi, endpointid), tokenProviderApi, policyApi, ref, null);
		command.execute(getRequest());
	}

	public Endpoint getMemberFromDriver(String endpointid) throws Exception {
		return catalogApi.getEndpoint(endpointid);
	}

}
