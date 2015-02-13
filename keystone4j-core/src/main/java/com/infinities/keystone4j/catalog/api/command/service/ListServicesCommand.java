package com.infinities.keystone4j.catalog.api.command.service;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.catalog.Service;

public class ListServicesCommand extends AbstractCatalogCommand implements TruncatedCommand<Service> {

	public ListServicesCommand(CatalogDriver catalogDriver) {
		super(catalogDriver);
	}

	@Override
	public List<Service> execute(Hints hints) throws Exception {
		if (hints == null) {
			hints = new Hints();
		}
		return this.getCatalogDriver().listServices(hints);
	}

}
