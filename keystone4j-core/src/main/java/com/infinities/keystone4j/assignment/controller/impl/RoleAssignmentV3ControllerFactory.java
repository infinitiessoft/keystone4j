package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.controller.RoleAssignmentV3Controller;
import com.infinities.keystone4j.common.BaseControllerFactory;

public class RoleAssignmentV3ControllerFactory extends BaseControllerFactory implements Factory<RoleAssignmentV3Controller> {

	@Inject
	public RoleAssignmentV3ControllerFactory() {
	}

	@Override
	public void dispose(RoleAssignmentV3Controller arg0) {

	}

	@Override
	public RoleAssignmentV3Controller provide() {
		RoleAssignmentV3ControllerImpl controller = new RoleAssignmentV3ControllerImpl();
		controller.setRequest(getRequest());
		return controller;
	}

}
