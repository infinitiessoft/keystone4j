package com.infinities.keystone4j.catalog.action.endpoint;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;

public class GetEndpointAction extends AbstractEndpointAction<Endpoint> {

	private String endpointid;


	public GetEndpointAction(CatalogApi catalogApi, String endpointid) {
		super(catalogApi);
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute() {
		return this.getCatalogApi().getEndpoint(endpointid);
	}
}
