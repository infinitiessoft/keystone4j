package com.infinities.keystone4j.catalog.controller;

import com.infinities.keystone4j.model.catalog.wrapper.EndpointWrapper;
import com.infinities.keystone4j.model.catalog.wrapper.EndpointsWrapper;

public interface EndpointController {

	EndpointsWrapper getEndpoints();

	EndpointWrapper createEndpoint();

	void deleteEndpoint();

}
