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
import com.infinities.keystone4j.decorator.FilterProtectedDecorator;
import com.infinities.keystone4j.decorator.ProtectedDecorator;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.EndpointWrapper;
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
				tokenProviderApi, policyApi, endpoint), tokenProviderApi, policyApi);
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
	public MemberWrapper<Endpoint> getEndpoint(String endpointid) {
		Endpoint ref = getMemberFromDriver(endpointid);
		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new GetEndpointAction(catalogApi,
				tokenProviderApi, policyApi, endpointid), tokenProviderApi, policyApi, ref);
		MemberWrapper<Endpoint> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public EndpointWrapper updateEndpoint(String endpointid, Endpoint endpoint) {
		Endpoint ref = getMemberFromDriver(endpointid);
		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new UpdateEndpointAction(catalogApi,
				tokenProviderApi, policyApi, endpointid, endpoint), tokenProviderApi, policyApi, ref);
		MemberWrapper<Endpoint> ret = command.execute(getRequest());
		return ret;
	}

	@Override
	public void deleteEndpoint(String endpointid) {
		Endpoint ref = getMemberFromDriver(endpointid);
		ProtectedAction<Endpoint> command = new ProtectedDecorator<Endpoint>(new DeleteEndpointAction(catalogApi,
				tokenProviderApi, policyApi, endpointid), tokenProviderApi, policyApi, ref);
		command.execute(getRequest());
	}

	public Endpoint getMemberFromDriver(String endpointid) {
		return catalogApi.getEndpoint(endpointid);
	}

}
