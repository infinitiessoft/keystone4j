package com.infinities.keystone4j.catalog.command.service;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.exception.NotFoundException;
import com.infinities.keystone4j.exception.ServiceNotFoundException;

public class DeleteServiceCommand extends AbstractCatalogCommand<Service> {

	private final String serviceid;


	public DeleteServiceCommand(CatalogApi catalogApi, CatalogDriver catalogDriver, String serviceid) {
		super(catalogApi, catalogDriver);
		this.serviceid = serviceid;
	}

	@Override
	public Service execute() {
		try {
			this.getCatalogDriver().deleteService(serviceid);
			return null;
		} catch (NotFoundException e) {
			throw new ServiceNotFoundException(null, serviceid);
		}
	}

}
