package com.infinities.keystone4j.assignment.action.project;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;

public abstract class AbstractProjectAction<T> implements Action<T> {

	protected AssignmentApi assignmentApi;


	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public AbstractProjectAction(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

}
