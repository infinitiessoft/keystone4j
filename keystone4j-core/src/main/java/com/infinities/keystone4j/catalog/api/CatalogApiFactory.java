package com.infinities.keystone4j.catalog.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;

public class CatalogApiFactory implements Factory<CatalogApi> {

	private final CatalogDriver catalogDriver;


	@Inject
	public CatalogApiFactory(CatalogDriver catalogDriver) {
		this.catalogDriver = catalogDriver;
	}

	@Override
	public void dispose(CatalogApi arg0) {

	}

	@Override
	public CatalogApi provide() {
		return new CatalogApiImpl(catalogDriver);
	}

}
