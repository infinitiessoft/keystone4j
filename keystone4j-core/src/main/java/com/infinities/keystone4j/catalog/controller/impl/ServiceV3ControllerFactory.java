package com.infinities.keystone4j.catalog.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.controller.ServiceV3Controller;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class ServiceV3ControllerFactory implements Factory<ServiceV3Controller> {

	private final CatalogApi catalogApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	@Inject
	public ServiceV3ControllerFactory(CatalogApi catalogApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(ServiceV3Controller arg0) {

	}

	@Override
	public ServiceV3Controller provide() {
		return new ServiceV3ControllerImpl(catalogApi, tokenApi, policyApi);
	}

}
