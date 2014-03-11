package com.infinities.keystone4j.catalog.command.endpoint;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.exception.Exceptions;

public class CreateEndpointCommand extends AbstractCatalogCommand<Endpoint> {

	private final Endpoint endpoint;


	public CreateEndpointCommand(CatalogApi catalogApi, CatalogDriver catalogDriver, Endpoint endpoint) {
		super(catalogApi, catalogDriver);
		this.endpoint = endpoint;
	}

	@Override
	public Endpoint execute() {
		try {
			return this.getCatalogDriver().createEndpoint(endpoint);
		} catch (Exception e) {
			throw Exceptions.ServiceNotFoundException.getInstance(null, endpoint.getService().getId());

		}
	}

}
