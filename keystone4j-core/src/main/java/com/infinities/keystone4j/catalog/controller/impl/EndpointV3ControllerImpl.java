package com.infinities.keystone4j.catalog.controller.impl;

import java.util.List;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.action.endpoint.CreateEndpointAction;
import com.infinities.keystone4j.catalog.action.endpoint.DeleteEndpointAction;
import com.infinities.keystone4j.catalog.action.endpoint.GetEndpointAction;
import com.infinities.keystone4j.catalog.action.endpoint.ListEndpointsAction;
import com.infinities.keystone4j.catalog.action.endpoint.UpdateEndpointAction;
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.catalog.model.EndpointWrapper;
import com.infinities.keystone4j.catalog.model.EndpointsWrapper;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class EndpointV3ControllerImpl implements EndpointV3Controller {

	private final CatalogApi catalogApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	public EndpointV3ControllerImpl(CatalogApi catalogApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public EndpointWrapper createEndpoint(Endpoint endpoint) {
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new CreateEndpointAction(catalogApi, endpoint), null,
				tokenApi, policyApi);
		Endpoint ret = command.execute();
		return new EndpointWrapper(ret);
	}

	@Override
	public EndpointsWrapper listEndpoints(String interfaceType, String serviceid, int page, int perPage) {
		Action<List<Endpoint>> command = new FilterCheckDecorator<List<Endpoint>>(new PaginateDecorator<Endpoint>(
				new ListEndpointsAction(catalogApi, interfaceType, serviceid), page, perPage));

		List<Endpoint> ret = command.execute();
		return new EndpointsWrapper(ret);
	}

	@Override
	public EndpointWrapper getEndpoint(String endpointid) {
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new GetEndpointAction(catalogApi, endpointid), null,
				tokenApi, policyApi);
		Endpoint ret = command.execute();
		return new EndpointWrapper(ret);
	}

	@Override
	public EndpointWrapper updateEndpoint(String endpointid, Endpoint endpoint) {
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new UpdateEndpointAction(catalogApi, endpointid,
				endpoint), null, tokenApi, policyApi);
		Endpoint ret = command.execute();
		return new EndpointWrapper(ret);
	}

	@Override
	public void deleteEndpoint(String endpointid) {
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new DeleteEndpointAction(catalogApi, endpointid),
				null, tokenApi, policyApi);
		command.execute();
	}

}
