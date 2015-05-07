/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
