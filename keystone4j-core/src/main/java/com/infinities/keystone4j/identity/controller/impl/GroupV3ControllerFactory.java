package com.infinities.keystone4j.identity.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.identity.controllers.GroupV3 20141211

public class GroupV3ControllerFactory extends BaseControllerFactory implements Factory<GroupV3Controller> {

	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	@Inject
	public GroupV3ControllerFactory(IdentityApi identityApi, AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi,
			PolicyApi policyApi) {
		this.identityApi = identityApi;
		this.identityApi.setAssignmentApi(assignmentApi);
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(GroupV3Controller arg0) {

	}

	@Override
	public GroupV3Controller provide() {
		GroupV3ControllerImpl controller = new GroupV3ControllerImpl(identityApi, tokenProviderApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
