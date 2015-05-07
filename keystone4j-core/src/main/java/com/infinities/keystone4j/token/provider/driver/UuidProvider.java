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
package com.infinities.keystone4j.token.provider.driver;

import java.util.UUID;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.trust.TrustApi;

public class UuidProvider extends BaseProvider {

	// private final static Logger logger =
	// LoggerFactory.getLogger(UuidProvider.class);

	public UuidProvider(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi, TrustApi trustApi) {
		super(assignmentApi, catalogApi, identityApi, trustApi);
	}

	@Override
	public String getTokenId(Object tokenData) {
		return UUID.randomUUID().toString();
	}
}
