package com.infinities.keystone4j.catalog.api.command.endpoint;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class GetEndpointCommand extends AbstractCatalogCommand implements NonTruncatedCommand<Endpoint> {

	private final String endpointid;


	public GetEndpointCommand(CatalogDriver catalogDriver, String endpointid) {
		super(catalogDriver);
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute() {
		try {
			return this.getCatalogDriver().getEndpoint(endpointid);
		} catch (Exception e) {
			throw Exceptions.EndpointNotFoundException.getInstance(null, endpointid);

		}
	}

}