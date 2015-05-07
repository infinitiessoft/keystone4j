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
package com.infinities.keystone4j.assignment.controller.action.role.v3;

import java.lang.reflect.Method;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Role;
import com.infinities.keystone4j.model.assignment.wrapper.RoleWrapper;
import com.infinities.keystone4j.model.assignment.wrapper.RolesWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractRoleAction extends AbstractAction<Role> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(AbstractDomainAction.class);
	protected AssignmentApi assignmentApi;
	protected Method getMemberFromDriver;


	public AbstractRoleAction(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.assignmentApi = assignmentApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	@Override
	public CollectionWrapper<Role> getCollectionWrapper() {
		return new RolesWrapper();
	}

	@Override
	public MemberWrapper<Role> getMemberWrapper() {
		return new RoleWrapper();
	}

	@Override
	public String getCollectionName() {
		return "roles";
	}

	@Override
	public String getMemberName() {
		return "role";
	}

}
