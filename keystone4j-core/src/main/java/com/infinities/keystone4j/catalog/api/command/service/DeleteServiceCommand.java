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
package com.infinities.keystone4j.catalog.api.command.service;

import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.catalog.api.command.AbstractCatalogCommand;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class DeleteServiceCommand extends AbstractCatalogCommand implements NotifiableCommand<Service> {

	private final String serviceid;


	// private final CatalogApi catalogApi;

	public DeleteServiceCommand(CatalogDriver catalogDriver, String serviceid) {
		super(catalogDriver);
		this.serviceid = serviceid;
	}

	@Override
	public Service execute() {
		try {
			// List<Endpoint> endpoints = catalogApi.listEndpoints(null);
			Service ret = this.getCatalogDriver().deleteService(serviceid);
			// TODO ignore self.get_service.invalidate(self, service_id)
			// for (Endpoint endpoint : endpoints) {
			// if (serviceid.equals(endpoint.getServiceid())) {
			//
			// }
			// }
			return ret;
		} catch (Exception e) {
			throw Exceptions.ServiceNotFoundException.getInstance(null, serviceid);
		}
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return serviceid;
		}
		throw new IllegalArgumentException("invalid index");
	}
}
