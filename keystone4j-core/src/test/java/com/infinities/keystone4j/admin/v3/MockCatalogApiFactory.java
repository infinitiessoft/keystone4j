package com.infinities.keystone4j.admin.v3;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogApi;

public class MockCatalogApiFactory implements Factory<CatalogApi> {

	private final CatalogApi catalogApi;


	public MockCatalogApiFactory(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	@Override
	public void dispose(CatalogApi arg0) {

	}

	@Override
	public CatalogApi provide() {
		return catalogApi;
	}

}
