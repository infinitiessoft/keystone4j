package com.infinities.keystone4j.catalog.api.command.service;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteServiceCommand extends AbstractCatalogCommand implements NotifiableCommand<Service> {

	private final String serviceid;


	// private final CatalogApi catalogApi;

	public DeleteServiceCommand(CatalogDriver catalogDriver, String serviceid) {
		super(catalogDriver);
		this.serviceid = serviceid;
	}

	@Override
	public Service execute() {
		try {
			// List<Endpoint> endpoints = catalogApi.listEndpoints(null);
			Service ret = this.getCatalogDriver().deleteService(serviceid);
			// TODO ignore self.get_service.invalidate(self, service_id)
			// for (Endpoint endpoint : endpoints) {
			// if (serviceid.equals(endpoint.getServiceid())) {
			//
			// }
			// }
			return ret;
		} catch (Exception e) {
			throw Exceptions.ServiceNotFoundException.getInstance(null, serviceid);
		}
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return serviceid;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
