package com.infinities.keystone4j.catalog.api.command.endpoint;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.catalog.Endpoint;

public class ListEndpointsCommand extends AbstractCatalogCommand implements TruncatedCommand<Endpoint> {

	public ListEndpointsCommand(CatalogDriver catalogDriver) {
		super(catalogDriver);
	}

	@Override
	public List<Endpoint> execute(Hints hints) throws Exception {
		if (hints == null) {
			hints = new Hints();
		}
		return this.getCatalogDriver().listEndpoints(hints);
	}

}
