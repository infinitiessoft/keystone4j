package com.infinities.keystone4j.catalog.api.command.region;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.model.catalog.Region;

public class ListRegionsCommand extends AbstractCatalogCommand implements TruncatedCommand<Region> {

	public ListRegionsCommand(CatalogDriver catalogDriver) {
		super(catalogDriver);
	}

	@Override
	public List<Region> execute(Hints hints) throws Exception {
		if (hints == null) {
			hints = new Hints();
		}
		return this.getCatalogDriver().listRegions(hints);
	}

}
