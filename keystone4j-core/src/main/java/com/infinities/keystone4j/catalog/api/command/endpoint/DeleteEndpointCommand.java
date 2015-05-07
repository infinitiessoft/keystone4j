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

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteEndpointCommand extends AbstractCatalogCommand implements NotifiableCommand<Endpoint> {

	private final String endpointid;


	public DeleteEndpointCommand(CatalogDriver catalogDriver, String endpointid) {
		super(catalogDriver);
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute() {
		try {
			this.getCatalogDriver().deleteEndpoint(endpointid);
			// TODO ignore self.get_endpoint.invalidate(self, endpoint_id)
			return null;
		} catch (Exception e) {
			throw Exceptions.EndpointNotFoundException.getInstance(null, endpointid);

		}
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return endpointid;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
