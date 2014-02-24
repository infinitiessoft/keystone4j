package com.infinities.keystone4j.catalog.command.endpoint;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.exception.NotFoundException;
import com.infinities.keystone4j.exception.ServiceNotFoundException;

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
		} catch (NotFoundException e) {
			throw new ServiceNotFoundException(null, endpoint.getService().getId());

		}
	}

}
