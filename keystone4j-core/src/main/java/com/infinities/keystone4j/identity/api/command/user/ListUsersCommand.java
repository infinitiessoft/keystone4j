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
package com.infinities.keystone4j.identity.api.command.user;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.identity.api.command.AbstractIdentityCommand;
import com.infinities.keystone4j.model.identity.User;
import com.infinities.keystone4j.model.identity.mapping.EntityType;

public class ListUsersCommand extends AbstractIdentityCommand implements TruncatedCommand<User> {

	// private final static String DEFAULT_DOMAIN_ID = "default_domain_id";
	private final String domainid;


	public ListUsersCommand(AssignmentApi assignmentApi, CredentialApi credentialApi, RevokeApi revokeApi,
			IdentityApi identityApi, IdMappingApi idMappingApi, IdentityDriver identityDriver, String domainid) {
		super(assignmentApi, credentialApi, revokeApi, identityApi, idMappingApi, identityDriver);
		this.domainid = domainid;
	}

	@Override
	public List<User> execute(Hints hints) throws Exception {
		IdentityDriver driver = selectIdentityDriver(domainid);
		if (hints == null) {
			hints = new Hints();
		}
		if (driver.isDomainAware()) {
			ensureDomainIdInHints(hints, domainid);
		} else {
			markDomainIdFilterSatisfied(hints);
		}
		List<User> refList = driver.listUsers(hints);

		return setDomainIdAndMapping(refList, domainid, driver, EntityType.USER);
	}
}
