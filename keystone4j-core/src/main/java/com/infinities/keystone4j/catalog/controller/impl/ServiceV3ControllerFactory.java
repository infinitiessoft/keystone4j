package com.infinities.keystone4j.catalog.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.controller.ServiceV3Controller;

public class ServiceV3ControllerFactory implements Factory<ServiceV3Controller> {

	private final CatalogApi catalogApi;


	@Inject
	public ServiceV3ControllerFactory(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	@Override
	public void dispose(ServiceV3Controller arg0) {

	}

	@Override
	public ServiceV3Controller provide() {
		return new ServiceV3ControllerImpl(catalogApi);
	}

}
