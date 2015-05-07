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
package com.infinities.keystone4j.assignment.api.command.domain;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.assignment.api.command.AbstractAssignmentCommand;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.notification.NotifiableCommand;

public class UpdateDomainCommand extends AbstractAssignmentCommand implements NotifiableCommand<Domain> {

	private final String domainid;
	private final Domain domain;


	public UpdateDomainCommand(CredentialApi credentialApi, IdentityApi identityApi, AssignmentApi assignmentApi,
			RevokeApi revokeApi, AssignmentDriver assignmentDriver, String domainid, Domain domain) {
		super(credentialApi, identityApi, assignmentApi, revokeApi, assignmentDriver);
		this.domainid = domainid;
		this.domain = domain;
	}

	@Override
	public Domain execute() throws Exception {
		Domain originalDomain = this.getAssignmentDriver().getDomain(domainid);
		Domain ret = this.getAssignmentDriver().updateDomain(domainid, domain);

		if (originalDomain.getEnabled() && !domain.getEnabled()) {
			this.getAssignmentApi().disableDomain(domainid);
		}
		// TODO ignore invalidate cache(getDomain, getDomainByName)

		return ret;
	}

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return domainid;
		} else if (index == 2) {
			return domain;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
