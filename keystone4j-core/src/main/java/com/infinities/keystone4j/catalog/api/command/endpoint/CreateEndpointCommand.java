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

public class CreateEndpointCommand extends AbstractCatalogCommand implements NotifiableCommand<Endpoint> {

	private final String endpointid;
	private final Endpoint endpoint;


	public CreateEndpointCommand(CatalogDriver catalogDriver, String endpointid, Endpoint endpoint) {
		super(catalogDriver);
		this.endpointid = endpointid;
		this.endpoint = endpoint;
	}

	@Override
	public Endpoint execute() {
		try {
			return this.getCatalogDriver().createEndpoint(endpointid, endpoint);
		} catch (Exception e) {
			throw Exceptions.ServiceNotFoundException.getInstance(null, endpoint.getService().getId());

		}
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return endpointid;
		} else if (index == 2) {
			return endpoint;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
