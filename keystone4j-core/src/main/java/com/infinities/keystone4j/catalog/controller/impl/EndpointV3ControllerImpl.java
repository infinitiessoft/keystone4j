package com.infinities.keystone4j.catalog.controller.impl;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.action.endpoint.CreateEndpointAction;
import com.infinities.keystone4j.catalog.action.endpoint.DeleteEndpointAction;
import com.infinities.keystone4j.catalog.action.endpoint.GetEndpointAction;
import com.infinities.keystone4j.catalog.action.endpoint.ListEndpointsAction;
import com.infinities.keystone4j.catalog.action.endpoint.UpdateEndpointAction;
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.common.BaseController;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.catalog.EndpointWrapper;
import com.infinities.keystone4j.model.catalog.EndpointsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class EndpointV3ControllerImpl extends BaseController implements EndpointV3Controller {

	private final CatalogApi catalogApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;
	private final Map<String, Object> parMap;


	public EndpointV3ControllerImpl(CatalogApi catalogApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
		parMap = Maps.newHashMap();
	}

	@Override
	public EndpointWrapper createEndpoint(Endpoint endpoint) {
		parMap.put("endpoint", endpoint);
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new CreateEndpointAction(catalogApi, endpoint), null,
				tokenApi, policyApi, parMap);
		Endpoint ret = command.execute(getRequest());
		return new EndpointWrapper(ret, getRequest());
	}

	@Override
	public EndpointsWrapper listEndpoints(String interfaceType, String serviceid, int page, int perPage) {
		parMap.put("serviceid", serviceid);
		parMap.put("interfaceType", interfaceType);
		Action<List<Endpoint>> command = new FilterCheckDecorator<List<Endpoint>>(new PaginateDecorator<Endpoint>(
				new ListEndpointsAction(catalogApi, interfaceType, serviceid), page, perPage), tokenApi, policyApi, parMap);

		List<Endpoint> ret = command.execute(getRequest());
		return new EndpointsWrapper(ret, getRequest());
	}

	@Override
	public EndpointWrapper getEndpoint(String endpointid) {
		parMap.put("endpointid", endpointid);
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new GetEndpointAction(catalogApi, endpointid), null,
				tokenApi, policyApi, parMap);
		Endpoint ret = command.execute(getRequest());
		return new EndpointWrapper(ret, getRequest());
	}

	@Override
	public EndpointWrapper updateEndpoint(String endpointid, Endpoint endpoint) {
		parMap.put("endpoint", endpoint);
		parMap.put("endpointid", endpointid);
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new UpdateEndpointAction(catalogApi, endpointid,
				endpoint), null, tokenApi, policyApi, parMap);
		Endpoint ret = command.execute(getRequest());
		return new EndpointWrapper(ret, getRequest());
	}

	@Override
	public void deleteEndpoint(String endpointid) {
		parMap.put("endpointid", endpointid);
		Action<Endpoint> command = new PolicyCheckDecorator<Endpoint>(new DeleteEndpointAction(catalogApi, endpointid),
				null, tokenApi, policyApi, parMap);
		command.execute(getRequest());
	}

}
