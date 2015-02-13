package com.infinities.keystone4j.catalog.api.command.endpoint;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class CreateEndpointCommand extends AbstractCatalogCommand implements NotifiableCommand<Endpoint> {

	private final String endpointid;
	private final Endpoint endpoint;


	public CreateEndpointCommand(CatalogDriver catalogDriver, String endpointid, Endpoint endpoint) {
		super(catalogDriver);
		this.endpointid = endpointid;
		this.endpoint = endpoint;
	}

	@Override
	public Endpoint execute() {
		try {
			return this.getCatalogDriver().createEndpoint(endpointid, endpoint);
		} catch (Exception e) {
			throw Exceptions.ServiceNotFoundException.getInstance(null, endpoint.getService().getId());

		}
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return endpointid;
		} else if (index == 2) {
			return endpoint;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
