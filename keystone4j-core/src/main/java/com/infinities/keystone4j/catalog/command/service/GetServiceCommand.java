package com.infinities.keystone4j.catalog.command.service;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Service;

public class GetServiceCommand extends AbstractCatalogCommand<Service> {

	private final String serviceid;


	public GetServiceCommand(CatalogDriver catalogDriver, String serviceid) {
		super(catalogDriver);
		this.serviceid = serviceid;
	}

	@Override
	public Service execute() {
		try {
			return this.getCatalogDriver().getService(serviceid);
		} catch (Exception e) {
			throw Exceptions.ServiceNotFoundException.getInstance(null, serviceid);
		}
	}

}
