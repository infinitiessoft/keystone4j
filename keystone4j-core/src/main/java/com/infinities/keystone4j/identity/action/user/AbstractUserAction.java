package com.infinities.keystone4j.identity.action.user;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public abstract class AbstractUserAction<T> implements Action<T> {

	protected AssignmentApi assignmentApi;
	protected IdentityApi identityApi;
	protected TokenApi tokenApi;


	public AbstractUserAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenApi tokenApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
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
