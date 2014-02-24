package com.infinities.keystone4j.catalog.action.service;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Service;

public class GetServiceAction extends AbstractServiceAction<Service> {

	private String serviceid;


	public GetServiceAction(CatalogApi catalogApi, String serviceid) {
		super(catalogApi);
		this.serviceid = serviceid;
	}

	@Override
	public Service execute() {
		return this.getCatalogApi().getService(serviceid);
	}
}
