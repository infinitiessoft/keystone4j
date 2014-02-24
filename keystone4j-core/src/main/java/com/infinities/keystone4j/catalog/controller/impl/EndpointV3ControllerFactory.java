package com.infinities.keystone4j.catalog.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;

public class EndpointV3ControllerFactory implements Factory<EndpointV3Controller> {

	private final CatalogApi catalogApi;


	@Inject
	public EndpointV3ControllerFactory(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	@Override
	public void dispose(EndpointV3Controller arg0) {

	}

	@Override
	public EndpointV3Controller provide() {
		return new EndpointV3ControllerImpl(catalogApi);
	}

}
