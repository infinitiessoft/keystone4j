package com.infinities.keystone4j.mock;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;

public class MockAssignmentApiFactory implements Factory<AssignmentApi> {

	private final AssignmentApi assignmentApi;


	public MockAssignmentApiFactory(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	@Override
	public void dispose(AssignmentApi arg0) {

	}

	@Override
	public AssignmentApi provide() {
		return assignmentApi;
	}

}
