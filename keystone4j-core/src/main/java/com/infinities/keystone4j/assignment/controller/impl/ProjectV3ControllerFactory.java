package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class ProjectV3ControllerFactory implements Factory<ProjectV3Controller> {

	private final AssignmentApi assignmentApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	@Inject
	public ProjectV3ControllerFactory(AssignmentApi assignmentApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(ProjectV3Controller arg0) {

	}

	@Override
	public ProjectV3Controller provide() {
		return new ProjectV3ControllerImpl(assignmentApi, tokenApi, policyApi);
	}

}
