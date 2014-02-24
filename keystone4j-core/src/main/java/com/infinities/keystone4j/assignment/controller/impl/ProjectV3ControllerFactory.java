package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.ProjectV3Controller;

public class ProjectV3ControllerFactory implements Factory<ProjectV3Controller> {

	private final AssignmentApi assignmentApi;


	@Inject
	public ProjectV3ControllerFactory(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	@Override
	public void dispose(ProjectV3Controller arg0) {

	}

	@Override
	public ProjectV3Controller provide() {
		return new ProjectV3ControllerImpl(assignmentApi);
	}

}
