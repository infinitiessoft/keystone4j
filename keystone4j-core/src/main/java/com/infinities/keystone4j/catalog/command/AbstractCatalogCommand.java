package com.infinities.keystone4j.catalog.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;

public abstract class AbstractCatalogCommand<T> implements Command<T> {

	private final CatalogApi catalogApi;
	private final CatalogDriver catalogDriver;


	public AbstractCatalogCommand(CatalogApi catalogApi, CatalogDriver catalogDriver) {
		super();
		this.catalogApi = catalogApi;
		this.catalogDriver = catalogDriver;
	}


	public CatalogApi getCatalogApi() {
		return catalogApi;
	}


	public CatalogDriver getCatalogDriver() {
		return catalogDriver;
	}
}
