package com.infinities.keystone4j.catalog.api.command.service;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class CreateServiceCommand extends AbstractCatalogCommand implements NotifiableCommand<Service> {

	private final Service service;


	public CreateServiceCommand(CatalogDriver catalogDriver, Service service) {
		super(catalogDriver);
		this.service = service;
	}

	@Override
	public Service execute() throws Exception {
		return this.getCatalogDriver().createService(service);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return service;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
