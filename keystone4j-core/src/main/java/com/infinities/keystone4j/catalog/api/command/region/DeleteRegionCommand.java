package com.infinities.keystone4j.catalog.api.command.region;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteRegionCommand extends AbstractCatalogCommand implements NotifiableCommand<Region> {

	private final String regionid;


	public DeleteRegionCommand(CatalogDriver catalogDriver, String regionid) {
		super(catalogDriver);
		this.regionid = regionid;
	}

	@Override
	public Region execute() {
		try {
			return this.getCatalogDriver().deleteRegion(regionid);
		} catch (Exception e) {
			throw Exceptions.RegionNotFoundException.getInstance(null, regionid);
		}
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return regionid;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
