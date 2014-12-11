package com.infinities.keystone4j.catalog.api.command.service;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Service;

public class UpdateServiceCommand extends AbstractCatalogCommand<Service> {

	private final Service service;
	private final String serviceid;


	public UpdateServiceCommand(CatalogDriver catalogDriver, String serviceid, Service service) {
		super(catalogDriver);
		this.service = service;
		this.serviceid = serviceid;
	}

	@Override
	public Service execute() {
		try {
			return this.getCatalogDriver().updateService(serviceid, service);
		} catch (Exception e) {
			throw Exceptions.ServiceNotFoundException.getInstance(null, serviceid);
		}
	}

}
