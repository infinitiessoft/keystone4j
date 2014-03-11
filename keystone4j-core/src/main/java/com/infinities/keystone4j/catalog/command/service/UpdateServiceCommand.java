package com.infinities.keystone4j.catalog.command.service;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.exception.Exceptions;

public class UpdateServiceCommand extends AbstractCatalogCommand<Service> {

	private final Service service;
	private final String serviceid;


	public UpdateServiceCommand(CatalogApi catalogApi, CatalogDriver catalogDriver, String serviceid, Service service) {
		super(catalogApi, catalogDriver);
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
