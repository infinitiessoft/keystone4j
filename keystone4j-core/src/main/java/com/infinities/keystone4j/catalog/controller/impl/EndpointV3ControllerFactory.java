package com.infinities.keystone4j.catalog.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.controller.EndpointV3Controller;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.catalog.controllers.EndpointV3 20141211

public class EndpointV3ControllerFactory extends BaseControllerFactory implements Factory<EndpointV3Controller> {

	private final CatalogApi catalogApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	@Inject
	public EndpointV3ControllerFactory(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(EndpointV3Controller arg0) {

	}

	@Override
	public EndpointV3Controller provide() {
		EndpointV3ControllerImpl controller = new EndpointV3ControllerImpl(catalogApi, tokenProviderApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
