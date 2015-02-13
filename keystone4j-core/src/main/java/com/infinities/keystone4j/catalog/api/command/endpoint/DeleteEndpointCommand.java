package com.infinities.keystone4j.catalog.api.command.endpoint;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteEndpointCommand extends AbstractCatalogCommand implements NotifiableCommand<Endpoint> {

	private final String endpointid;


	public DeleteEndpointCommand(CatalogDriver catalogDriver, String endpointid) {
		super(catalogDriver);
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute() {
		try {
			this.getCatalogDriver().deleteEndpoint(endpointid);
			// TODO ignore self.get_endpoint.invalidate(self, endpoint_id)
			return null;
		} catch (Exception e) {
			throw Exceptions.EndpointNotFoundException.getInstance(null, endpointid);

		}
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return endpointid;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
