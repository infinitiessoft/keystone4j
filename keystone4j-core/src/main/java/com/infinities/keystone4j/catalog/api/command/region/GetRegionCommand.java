package com.infinities.keystone4j.catalog.api.command.region;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Region;

public class GetRegionCommand extends AbstractCatalogCommand implements NonTruncatedCommand<Region> {

	private final String regionid;


	public GetRegionCommand(CatalogDriver catalogDriver, String regionid) {
		super(catalogDriver);
		this.regionid = regionid;
	}

	@Override
	public Region execute() {
		try {
			return getRegion(regionid);
		} catch (Exception e) {
			throw Exceptions.RegionNotFoundException.getInstance(null, regionid);
		}
	}

}
