package com.infinities.keystone4j.catalog.api.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.catalog.CatalogDriver;

public abstract class AbstractCatalogCommand<T> implements Command<T> {

	private final CatalogDriver catalogDriver;


	public AbstractCatalogCommand(CatalogDriver catalogDriver) {
		this.catalogDriver = catalogDriver;
	}

	public CatalogDriver getCatalogDriver() {
		return catalogDriver;
	}

}
