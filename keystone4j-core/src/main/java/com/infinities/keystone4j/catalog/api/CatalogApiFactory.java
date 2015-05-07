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
package com.infinities.keystone4j.catalog.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.CatalogDriver;

public class CatalogApiFactory implements Factory<CatalogApi> {

	private final CatalogDriver catalogDriver;


	@Inject
	public CatalogApiFactory(CatalogDriver catalogDriver) {
		this.catalogDriver = catalogDriver;
	}

	@Override
	public void dispose(CatalogApi arg0) {

	}

	@Override
	public CatalogApi provide() {
		return new CatalogApiImpl(catalogDriver);
	}

}
