package com.infinities.keystone4j.assignment.action.grant;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;

public abstract class AbstractGrantAction<T> implements Action<T> {

	protected AssignmentApi assignmentApi;
	protected IdentityApi identityApi;


	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public AbstractGrantAction(AssignmentApi assignmentApi, IdentityApi identityApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

}
