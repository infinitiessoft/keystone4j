package com.infinities.keystone4j.catalog.api.command.service;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.model.catalog.Service;

public class CreateServiceCommand extends AbstractCatalogCommand<Service> {

	private final Service service;


	public CreateServiceCommand(CatalogDriver catalogDriver, Service service) {
		super(catalogDriver);
		this.service = service;
	}

	@Override
	public Service execute() {
		return this.getCatalogDriver().createService(service);
	}

}
