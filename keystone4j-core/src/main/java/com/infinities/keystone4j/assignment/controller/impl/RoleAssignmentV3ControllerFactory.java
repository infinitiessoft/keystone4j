package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers.RoleAssignmentV3 20141210

public class RoleAssignmentV3ControllerFactory extends BaseControllerFactory implements Factory<RoleAssignmentV3Controller> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	@Inject
	public RoleAssignmentV3ControllerFactory(AssignmentApi assignmentApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(RoleAssignmentV3Controller arg0) {

	}

	@Override
	public RoleAssignmentV3Controller provide() {
		RoleAssignmentV3ControllerImpl controller = new RoleAssignmentV3ControllerImpl(assignmentApi, identityApi,
				tokenProviderApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
