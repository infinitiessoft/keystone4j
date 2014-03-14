package com.infinities.keystone4j.catalog.action.endpoint;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;

public class GetEndpointAction extends AbstractEndpointAction<Endpoint> {

	private final String endpointid;


	public GetEndpointAction(CatalogApi catalogApi, String endpointid) {
		super(catalogApi);
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute(ContainerRequestContext request) {
		return this.getCatalogApi().getEndpoint(endpointid);
	}

	@Override
	public String getName() {
		return "get_endpoint";
	}
}
