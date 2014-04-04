package com.infinities.keystone4j.catalog.action.service;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.catalog.Service;

public class UpdateServiceAction extends AbstractServiceAction<Service> {

	private final String serviceid;
	private final Service service;


	public UpdateServiceAction(CatalogApi catalogApi, String serviceid, Service service) {
		super(catalogApi);
		this.serviceid = serviceid;
		this.service = service;
	}

	@Override
	public Service execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(serviceid, service);
		return this.getCatalogApi().updateService(serviceid, service);
	}

	@Override
	public String getName() {
		return "update_service";
	}
}
