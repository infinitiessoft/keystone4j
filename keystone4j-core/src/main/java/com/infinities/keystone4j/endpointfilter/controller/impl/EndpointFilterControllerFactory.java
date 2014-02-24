package com.infinities.keystone4j.endpointfilter.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
import com.infinities.keystone4j.endpointfilter.controller.EndpointFilterController;

public class EndpointFilterControllerFactory implements Factory<EndpointFilterController> {

	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final EndpointFilterApi endpointFilterApi;


	@Inject
	public EndpointFilterControllerFactory(AssignmentApi assignmentApi, CatalogApi catalogApi,
			EndpointFilterApi endpointFilterApi) {
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.endpointFilterApi = endpointFilterApi;
	}

	@Override
	public void dispose(EndpointFilterController arg0) {

	}

	@Override
	public EndpointFilterController provide() {
		return new EndpointFilterControllerImpl(assignmentApi, catalogApi, endpointFilterApi);
	}

}
