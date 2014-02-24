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

public class EndpointV3ControllerImpl implements EndpointV3Controller {

	private CatalogApi catalogApi;


	public EndpointV3ControllerImpl(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	@Override
	public EndpointWrapper createEndpoint(Endpoint endpoint) {
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new CreateEndpointAction(catalogApi, endpoint));
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
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new GetEndpointAction(catalogApi, endpointid));
		Endpoint ret = command.execute();
		return new EndpointWrapper(ret);
	}

	@Override
	public EndpointWrapper updateEndpoint(String endpointid, Endpoint endpoint) {
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new UpdateEndpointAction(catalogApi, endpointid,
				endpoint));
		Endpoint ret = command.execute();
		return new EndpointWrapper(ret);
	}

	@Override
	public void deleteEndpoint(String endpointid) {
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new DeleteEndpointAction(catalogApi, endpointid));
		command.execute();
	}

}
