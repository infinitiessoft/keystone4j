package com.infinities.keystone4j.catalog.action.service;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Service;

public class CreateServiceAction extends AbstractServiceAction<Service> {

	private final Service service;


	public CreateServiceAction(CatalogApi catalogApi, Service service) {
		super(catalogApi);
		this.service = service;
	}

	@Override
	public Service execute() {
		Service ret = catalogApi.createService(service);
		return ret;
	}

	@Override
	public String getName() {
		return "create_service";
	}
}
