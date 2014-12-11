package com.infinities.keystone4j.catalog.api.command.endpoint;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class UpdateEndpointCommand extends AbstractCatalogCommand<Endpoint> {

	private final String endpointid;
	private final Endpoint endpoint;


	public UpdateEndpointCommand(CatalogDriver catalogDriver, String endpointid, Endpoint endpoint) {
		super(catalogDriver);
		this.endpointid = endpointid;
		this.endpoint = endpoint;
	}

	@Override
	public Endpoint execute() {
		return this.getCatalogDriver().updateEndpoint(endpointid, endpoint);
	}

}
