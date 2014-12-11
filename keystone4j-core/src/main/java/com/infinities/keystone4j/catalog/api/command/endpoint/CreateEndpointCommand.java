package com.infinities.keystone4j.catalog.api.command.endpoint;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class CreateEndpointCommand extends AbstractCatalogCommand<Endpoint> {

	private final Endpoint endpoint;


	public CreateEndpointCommand(CatalogDriver catalogDriver, Endpoint endpoint) {
		super(catalogDriver);
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
