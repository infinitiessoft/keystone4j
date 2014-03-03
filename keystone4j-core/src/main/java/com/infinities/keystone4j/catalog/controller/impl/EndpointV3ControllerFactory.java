package com.infinities.keystone4j.catalog.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class EndpointV3ControllerFactory implements Factory<EndpointV3Controller> {

	private final CatalogApi catalogApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	@Inject
	public EndpointV3ControllerFactory(CatalogApi catalogApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(EndpointV3Controller arg0) {

	}

	@Override
	public EndpointV3Controller provide() {
		return new EndpointV3ControllerImpl(catalogApi, tokenApi, policyApi);
	}

}
