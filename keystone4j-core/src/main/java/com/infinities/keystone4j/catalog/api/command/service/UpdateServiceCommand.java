package com.infinities.keystone4j.catalog.api.command.service;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class UpdateServiceCommand extends AbstractCatalogCommand implements NotifiableCommand<Service> {

	private final Service service;
	private final String serviceid;


	public UpdateServiceCommand(CatalogDriver catalogDriver, String serviceid, Service service) {
		super(catalogDriver);
		this.service = service;
		this.serviceid = serviceid;
	}

	@Override
	public Service execute() throws Exception {
		return this.getCatalogDriver().updateService(serviceid, service);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return serviceid;
		} else if (index == 2) {
			return service;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
