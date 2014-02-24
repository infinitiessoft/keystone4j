package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.RoleV3Controller;
import com.infinities.keystone4j.identity.IdentityApi;

public class RoleV3ControllerFactory implements Factory<RoleV3Controller> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;


	@Inject
	public RoleV3ControllerFactory(AssignmentApi assignmentApi, IdentityApi identityApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
	}

	@Override
	public void dispose(RoleV3Controller arg0) {

	}

	@Override
	public RoleV3Controller provide() {
		return new RoleV3ControllerImpl(assignmentApi, identityApi);
	}

}
