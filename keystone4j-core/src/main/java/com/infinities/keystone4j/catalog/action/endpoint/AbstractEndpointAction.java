package com.infinities.keystone4j.catalog.action.endpoint;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.catalog.CatalogApi;

public abstract class AbstractEndpointAction<T> implements Action<T> {

	protected CatalogApi catalogApi;


	public AbstractEndpointAction(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

	public CatalogApi getCatalogApi() {
		return catalogApi;
	}

	public void setCatalogApi(CatalogApi catalogApi) {
		this.catalogApi = catalogApi;
	}

}
