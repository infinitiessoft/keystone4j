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
