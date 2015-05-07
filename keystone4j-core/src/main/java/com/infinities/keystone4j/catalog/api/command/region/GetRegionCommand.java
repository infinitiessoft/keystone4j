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
