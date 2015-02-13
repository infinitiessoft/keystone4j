package com.infinities.keystone4j.catalog.api.command;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.model.catalog.Region;

public abstract class AbstractCatalogCommand {

	private final CatalogDriver catalogDriver;


	public AbstractCatalogCommand(CatalogDriver catalogDriver) {
		this.catalogDriver = catalogDriver;
	}

	public CatalogDriver getCatalogDriver() {
		return catalogDriver;
	}

	protected Region getRegion(String id) throws Exception {
		return this.getCatalogDriver().getRegion(id);
	}

}
