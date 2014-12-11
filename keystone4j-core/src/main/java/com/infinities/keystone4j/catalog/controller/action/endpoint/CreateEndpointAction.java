package com.infinities.keystone4j.catalog.controller.action.endpoint;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CreateEndpointAction extends AbstractEndpointAction<Endpoint> {

	private final Endpoint endpoint;


	public CreateEndpointAction(CatalogApi catalogApi, TokenProviderApi tokenProviderApi, Endpoint endpoint) {
		super(catalogApi);
		this.endpoint = endpoint;
	}

	@Override
	public Endpoint execute(ContainerRequestContext request) {
		Endpoint ret = catalogApi.createEndpoint(endpoint);
		return ret;
	}

	@Override
	public String getName() {
		return "create_endpoint";
	}
}
