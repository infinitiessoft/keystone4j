package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.model.catalog.EndpointWrapper;
import com.infinities.keystone4j.model.catalog.EndpointsWrapper;

public interface EndpointController {

	EndpointsWrapper getEndpoints();

	EndpointWrapper createEndpoint();

	void deleteEndpoint();

}
