package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.catalog.model.EndpointWrapper;
import com.infinities.keystone4j.catalog.model.EndpointsWrapper;

public interface EndpointController {

	EndpointsWrapper getEndpoints();

	EndpointWrapper createEndpoint();

	void deleteEndpoint();

}
