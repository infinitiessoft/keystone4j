package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers.ProjectV3 20141209

public class ProjectV3ControllerFactory extends BaseControllerFactory implements Factory<ProjectV3Controller> {

	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	@Inject
	public ProjectV3ControllerFactory(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(ProjectV3Controller arg0) {

	}

	@Override
	public ProjectV3Controller provide() {
		ProjectV3ControllerImpl controller = new ProjectV3ControllerImpl(assignmentApi, tokenProviderApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
