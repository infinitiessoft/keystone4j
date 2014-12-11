package com.infinities.keystone4j.catalog.api.command;

import java.util.List;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.model.catalog.Catalog;
import com.infinities.keystone4j.model.catalog.Service;

public class GetV3CatalogCommand extends AbstractCatalogCommand<Catalog> {

	// private final String userid;
	// private final String projectid;

	public GetV3CatalogCommand(CatalogDriver catalogDriver, String userid, String projectid) {
		super(catalogDriver);
		// this.userid = userid;
		// this.projectid = projectid;
	}

	@Override
	public Catalog execute() {
		List<Service> services = this.getCatalogDriver().listServices();

		Catalog catalog = new Catalog();
		catalog.setServices(services);
		return catalog;
	}

}
