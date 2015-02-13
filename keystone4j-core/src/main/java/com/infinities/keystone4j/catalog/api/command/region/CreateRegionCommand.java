package com.infinities.keystone4j.catalog.api.command.region;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Region;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class CreateRegionCommand extends AbstractCatalogCommand implements NotifiableCommand<Region> {

	private final Region regionRef;


	public CreateRegionCommand(CatalogDriver catalogDriver, Region region) {
		super(catalogDriver);
		this.regionRef = region;
	}

	@Override
	public Region execute() {
		try {
			getRegion(regionRef.getId());
			String msg = String.format("Duplicate ID, %s", regionRef.getId());
			throw Exceptions.ConflictException.getInstance(null, "region", msg);
		} catch (Exception e) {

		}

		regionRef.setDescription("");
		try {
			return this.getCatalogDriver().createRegion(regionRef);
		} catch (Exception e) {
			String parentRegionId = regionRef.getParentRegionId();
			throw Exceptions.RegionNotFoundException.getInstance(null, parentRegionId);

		}
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return regionRef;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
