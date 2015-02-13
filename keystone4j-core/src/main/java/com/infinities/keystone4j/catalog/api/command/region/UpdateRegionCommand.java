package com.infinities.keystone4j.catalog.api.command.region;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class UpdateRegionCommand extends AbstractCatalogCommand implements NotifiableCommand<Region> {

	private final String regionid;
	private final Region regionRef;


	public UpdateRegionCommand(CatalogDriver catalogDriver, String regionid, Region regionRef) {
		super(catalogDriver);
		this.regionid = regionid;
		this.regionRef = regionRef;
	}

	@Override
	public Region execute() throws Exception {
		return this.getCatalogDriver().updateRegion(regionid, regionRef);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return regionid;
		} else if (index == 2) {
			return regionRef;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
