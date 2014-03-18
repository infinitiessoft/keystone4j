package com.infinities.keystone4j.identity.action.group;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public abstract class AbstractGroupAction<T> implements Action<T> {

	protected IdentityApi identityApi;
	protected AssignmentApi assignmentApi;
	protected TokenApi tokenApi;


	public AbstractGroupAction(AssignmentApi assignmentApi, TokenApi tokenApi, IdentityApi identityApi) {
		this.identityApi = identityApi;
		this.assignmentApi = assignmentApi;
		this.tokenApi = tokenApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

	public TokenApi getTokenApi() {
		return tokenApi;
	}

	public void setTokenApi(TokenApi tokenApi) {
		this.tokenApi = tokenApi;
	}

}
