package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.DomainV3Controller;

public class DomainV3ControllerFactory implements Factory<DomainV3Controller> {

	private final AssignmentApi assignmentApi;


	@Inject
	public DomainV3ControllerFactory(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	@Override
	public void dispose(DomainV3Controller arg0) {

	}

	@Override
	public DomainV3Controller provide() {
		return new DomainV3ControllerImpl(assignmentApi);
	}

}
