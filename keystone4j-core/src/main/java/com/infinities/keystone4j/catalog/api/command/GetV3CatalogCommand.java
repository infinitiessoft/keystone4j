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
package com.infinities.keystone4j.catalog.api.command;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.catalog.Service;

public class GetV3CatalogCommand extends AbstractCatalogCommand implements NonTruncatedCommand<List<Service>> {

	private final static Logger logger = LoggerFactory.getLogger(GetV3CatalogCommand.class);
	private final String userid;
	private final String projectid;


	public GetV3CatalogCommand(CatalogDriver catalogDriver, String userid, String projectid) {
		super(catalogDriver);
		this.userid = userid;
		this.projectid = projectid;
	}

	@Override
	public List<Service> execute() {
		try {
			return this.getCatalogDriver().getV3Catalog(userid, projectid);
		} catch (Exception e) {
			logger.warn("get catalog failed", e);
			throw Exceptions.NotFoundException.getInstance("Catalog not found for user and tenant");
		}
	}
}
