package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;

public class RoleAssignmentV3ControllerFactory implements Factory<RoleAssignmentV3Controller> {

	@Inject
	public RoleAssignmentV3ControllerFactory() {
	}

	@Override
	public void dispose(RoleAssignmentV3Controller arg0) {

	}

	@Override
	public RoleAssignmentV3Controller provide() {
		return new RoleAssignmentV3ControllerImpl();
	}

}
