package com.infinities.keystone4j.catalog.action.endpoint;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;

public class DeleteEndpointAction extends AbstractEndpointAction<Endpoint> {

	private final String endpointid;


	public DeleteEndpointAction(CatalogApi catalogApi, String endpointid) {
		super(catalogApi);
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute(ContainerRequestContext request) {
		return this.getCatalogApi().deleteEndpoint(endpointid);
	}

	@Override
	public String getName() {
		return "delete_endpoint";
	}
}
