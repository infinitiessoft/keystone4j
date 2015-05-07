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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.catalog.CatalogDriver;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.token.Metadata;

public class GetCatalogCommand extends AbstractCatalogCommand implements
		NonTruncatedCommand<Map<String, Map<String, Map<String, String>>>> {

	private final static Logger logger = LoggerFactory.getLogger(GetCatalogCommand.class);
	private final String userid;
	private final String tenantid;
	private final Metadata metadata;


	public GetCatalogCommand(CatalogDriver catalogDriver, String userid, String tenantid, Metadata metadata) {
		super(catalogDriver);
		this.userid = userid;
		this.tenantid = tenantid;
		this.metadata = metadata;
	}

	@Override
	public Map<String, Map<String, Map<String, String>>> execute() {
		try {
			return this.getCatalogDriver().getCatalog(userid, tenantid, metadata);
		} catch (Exception e) {
			logger.warn("get catalog failed", e);
			throw Exceptions.NotFoundException.getInstance("Catalog not found for user and tenant");
		}
	}

}
