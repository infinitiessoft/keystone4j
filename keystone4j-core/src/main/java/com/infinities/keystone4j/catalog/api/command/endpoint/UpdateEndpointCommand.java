package com.infinities.keystone4j.catalog.api.command.endpoint;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class UpdateEndpointCommand extends AbstractCatalogCommand implements NotifiableCommand<Endpoint> {

	private final String endpointid;
	private final Endpoint endpoint;


	public UpdateEndpointCommand(CatalogDriver catalogDriver, String endpointid, Endpoint endpoint) {
		super(catalogDriver);
		this.endpointid = endpointid;
		this.endpoint = endpoint;
	}

	@Override
	public Endpoint execute() throws Exception {
		return this.getCatalogDriver().updateEndpoint(endpointid, endpoint);
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
