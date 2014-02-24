package com.infinities.keystone4j.catalog.command.service;

import java.util.List;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.catalog.model.Service;

public class ListServicesCommand extends AbstractCatalogCommand<List<Service>> {

	public ListServicesCommand(CatalogApi catalogApi, CatalogDriver catalogDriver) {
		super(catalogApi, catalogDriver);
	}

	@Override
	public List<Service> execute() {
		return this.getCatalogDriver().listServices();
	}

}
