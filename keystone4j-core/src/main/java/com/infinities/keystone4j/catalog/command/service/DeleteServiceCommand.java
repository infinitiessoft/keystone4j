package com.infinities.keystone4j.catalog.command.service;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.exception.Exceptions;

public class DeleteServiceCommand extends AbstractCatalogCommand<Service> {

	private final String serviceid;


	public DeleteServiceCommand(CatalogDriver catalogDriver, String serviceid) {
		super(catalogDriver);
		this.serviceid = serviceid;
	}

	@Override
	public Service execute() {
		try {
			this.getCatalogDriver().deleteService(serviceid);
			return null;
		} catch (Exception e) {
			throw Exceptions.ServiceNotFoundException.getInstance(null, serviceid);
		}
	}

}
