package com.infinities.keystone4j.catalog.action.service;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.catalog.Service;

public class DeleteServiceAction extends AbstractServiceAction<Service> {

	private final String serviceid;


	public DeleteServiceAction(CatalogApi catalogApi, String serviceid) {
		super(catalogApi);
		this.serviceid = serviceid;
	}

	@Override
	public Service execute(ContainerRequestContext request) {
		return this.getCatalogApi().deleteService(serviceid);
	}

	@Override
	public String getName() {
		return "delete_service";
	}
}
