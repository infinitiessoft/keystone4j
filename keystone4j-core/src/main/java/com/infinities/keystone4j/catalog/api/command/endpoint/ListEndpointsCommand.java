package com.infinities.keystone4j.catalog.api.command.endpoint;

import java.util.List;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class ListEndpointsCommand extends AbstractCatalogCommand<List<Endpoint>> {

	public ListEndpointsCommand(CatalogDriver catalogDriver) {
		super(catalogDriver);
	}

	@Override
	public List<Endpoint> execute() {
		return this.getCatalogDriver().listEndpoints();
	}

}
