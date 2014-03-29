package com.infinities.keystone4j.mock;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentDriver;

public class MockAssignmentDriverFactory implements Factory<AssignmentDriver> {

	private final AssignmentDriver assignmentDriver;


	public MockAssignmentDriverFactory(AssignmentDriver assignmentDriver) {
		this.assignmentDriver = assignmentDriver;
	}

	@Override
	public void dispose(AssignmentDriver arg0) {

	}

	@Override
	public AssignmentDriver provide() {
		return assignmentDriver;
	}

}
