package com.infinities.keystone4j.catalog.action.endpoint;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;

public class CreateEndpointAction extends AbstractEndpointAction<Endpoint> {

	private final Endpoint endpoint;


	public CreateEndpointAction(CatalogApi catalogApi, Endpoint endpoint) {
		super(catalogApi);
		this.endpoint = endpoint;
	}

	@Override
	public Endpoint execute() {
		Endpoint ret = catalogApi.createEndpoint(endpoint);
		return ret;
	}

	@Override
	public String getName() {
		return "create_endpoint";
	}
}
