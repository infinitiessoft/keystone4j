package com.infinities.keystone4j.assignment.action.role.v3;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;

public abstract class AbstractRoleAction<T> implements Action<T> {

	protected AssignmentApi assignmentApi;


	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public AbstractRoleAction(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

}
