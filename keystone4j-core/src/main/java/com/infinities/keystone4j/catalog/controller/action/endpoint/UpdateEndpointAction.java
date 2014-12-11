package com.infinities.keystone4j.catalog.controller.action.endpoint;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class UpdateEndpointAction extends AbstractEndpointAction<Endpoint> {

	private final String endpointid;
	private final Endpoint endpoint;


	public UpdateEndpointAction(CatalogApi catalogApi, String endpointid, Endpoint endpoint) {
		super(catalogApi);
		this.endpoint = endpoint;
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(endpointid, endpoint);
		return this.getCatalogApi().updateEndpoint(endpointid, endpoint);
	}

	@Override
	public String getName() {
		return "update_endpoint";
	}
}
