package com.infinities.keystone4j.catalog.command.endpoint;

import java.util.List;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.catalog.model.Endpoint;

public class ListEndpointsCommand extends AbstractCatalogCommand<List<Endpoint>> {

	public ListEndpointsCommand(CatalogApi catalogApi, CatalogDriver catalogDriver) {
		super(catalogApi, catalogDriver);
	}

	@Override
	public List<Endpoint> execute() {
		return this.getCatalogDriver().listEndpoints();
	}

}
