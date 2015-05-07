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
package com.infinities.keystone4j.assignment.controller.action.domain;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteDomainAction extends AbstractDomainAction implements ProtectedAction<Domain> {

	private final static Logger logger = LoggerFactory.getLogger(DeleteDomainAction.class);
	private final String domainid;


	public DeleteDomainAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String domainid) {
		super(assignmentApi, tokenProviderApi, policyApi);
		this.domainid = domainid;
	}

	@Override
	public MemberWrapper<Domain> execute(ContainerRequestContext request) throws Exception {
		logger.debug("delete domain: {}", domainid);
		this.getAssignmentApi().deleteDomain(domainid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_domain";
	}

}
