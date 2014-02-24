package com.infinities.keystone4j.catalog.action.service;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.catalog.CatalogApi;

public abstract class AbstractServiceAction<T> implements Action<T> {

	protected CatalogApi catalogApi;


	public AbstractServiceAction(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	public CatalogApi getCatalogApi() {
		return catalogApi;
	}

	public void setCatalogApi(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

}
