package com.infinities.keystone4j.catalog.action.endpoint;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;

public class UpdateEndpointAction extends AbstractEndpointAction<Endpoint> {

	private String endpointid;
	private Endpoint endpoint;


	public UpdateEndpointAction(CatalogApi catalogApi, String endpointid, Endpoint endpoint) {
		super(catalogApi);
	}

	@Override
	public Endpoint execute() {
		KeystonePreconditions.requireMatchingId(endpointid, endpoint);
		return this.getCatalogApi().updateEndpoint(endpointid, endpoint);
	}

	@Override
	public String getName() {
		return "update_endpoint";
	}
}
