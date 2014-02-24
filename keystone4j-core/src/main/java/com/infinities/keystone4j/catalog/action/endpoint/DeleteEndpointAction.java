package com.infinities.keystone4j.catalog.action.endpoint;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;

public class DeleteEndpointAction extends AbstractEndpointAction<Endpoint> {

	private String endpointid;


	public DeleteEndpointAction(CatalogApi catalogApi, String endpointid) {
		super(catalogApi);
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute() {
		return this.getCatalogApi().deleteEndpoint(endpointid);
	}

}
