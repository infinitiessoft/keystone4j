package com.infinities.keystone4j.assignment.action.domain;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;

public abstract class AbstractDomainAction<T> implements Action<T> {

	protected AssignmentApi assignmentApi;


	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public AbstractDomainAction(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

}
