package com.infinities.keystone4j.catalog.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.controller.RegionV3Controller;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.catalog.controllers.RegionV3 20141211

public class RegionV3ControllerFactory extends BaseControllerFactory implements Factory<RegionV3Controller> {

	private final CatalogApi catalogApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	@Inject
	public RegionV3ControllerFactory(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.catalogApi = catalogApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(RegionV3Controller arg0) {

	}

	@Override
	public RegionV3Controller provide() {
		RegionV3ControllerImpl controller = new RegionV3ControllerImpl(catalogApi, tokenProviderApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
