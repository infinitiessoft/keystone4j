package com.infinities.keystone4j.catalog.command.endpoint;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.command.AbstractCatalogCommand;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.exception.EndpointNotFoundException;
import com.infinities.keystone4j.exception.NotFoundException;

public class DeleteEndpointCommand extends AbstractCatalogCommand<Endpoint> {

	private final String endpointid;


	public DeleteEndpointCommand(CatalogApi catalogApi, CatalogDriver catalogDriver, String endpointid) {
		super(catalogApi, catalogDriver);
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute() {
		try {
			this.getCatalogDriver().deleteEndpoint(endpointid);
			return null;
		} catch (NotFoundException e) {
			throw new EndpointNotFoundException(null, endpointid);

		}
	}

}
