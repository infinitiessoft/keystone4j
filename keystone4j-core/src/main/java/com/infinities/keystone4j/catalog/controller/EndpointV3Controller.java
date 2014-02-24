package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.catalog.model.EndpointWrapper;
import com.infinities.keystone4j.catalog.model.EndpointsWrapper;

public interface EndpointV3Controller {

	EndpointWrapper createEndpoint(Endpoint endpoint);

	EndpointsWrapper listEndpoints(String interfaceType, String serviceid, int page, int perPage);

	EndpointWrapper getEndpoint(String endpointid);

	EndpointWrapper updateEndpoint(String endpointid, Endpoint endpoint);

	void deleteEndpoint(String endpointid);

}
