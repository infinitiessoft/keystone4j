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
import com.infinities.keystone4j.model.catalog.Service;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class UpdateServiceCommand extends AbstractCatalogCommand implements NotifiableCommand<Service> {

	private final Service service;
	private final String serviceid;


	public UpdateServiceCommand(CatalogDriver catalogDriver, String serviceid, Service service) {
		super(catalogDriver);
		this.service = service;
		this.serviceid = serviceid;
	}

	@Override
	public Service execute() throws Exception {
		return this.getCatalogDriver().updateService(serviceid, service);
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return serviceid;
		} else if (index == 2) {
			return service;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
