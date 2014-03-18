package com.infinities.keystone4j.identity.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.controller.GroupV3Controller;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class GroupV3ControllerFactory extends BaseControllerFactory implements Factory<GroupV3Controller> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	@Inject
	public GroupV3ControllerFactory(AssignmentApi assignmentApi, IdentityApi identityApi, TokenApi tokenApi,
			PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(GroupV3Controller arg0) {

	}

	@Override
	public GroupV3Controller provide() {
		GroupV3ControllerImpl controller = new GroupV3ControllerImpl(assignmentApi, identityApi, tokenApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
