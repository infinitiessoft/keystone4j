package com.infinities.keystone4j.catalog.command.service;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.catalog.model.Service;
import com.infinities.keystone4j.exception.Exceptions;

public class GetServiceCommand extends AbstractCatalogCommand<Service> {

	private final String serviceid;


	public GetServiceCommand(CatalogApi catalogApi, CatalogDriver catalogDriver, String serviceid) {
		super(catalogApi, catalogDriver);
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
