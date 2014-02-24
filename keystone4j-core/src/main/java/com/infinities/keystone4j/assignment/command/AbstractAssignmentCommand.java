package com.infinities.keystone4j.assignment.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public abstract class AbstractAssignmentCommand<T> implements Command<T> {

	private CredentialApi credentialApi;
	private IdentityApi identityApi;
	private TokenApi tokenApi;
	private AssignmentApi assignmentApi;
	private AssignmentDriver assignmentDriver;


	public AbstractAssignmentCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver) {
		super();
		this.credentialApi = credentialApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.setAssignmentApi(assignmentApi);
		this.assignmentDriver = assignmentDriver;
	}

	public CredentialApi getCredentialApi() {
		return credentialApi;
	}

	public void setCredentialApi(CredentialApi credentialApi) {
		this.credentialApi = credentialApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public void setIdentityApi(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	public TokenApi getTokenApi() {
		return tokenApi;
	}

	public void setTokenApi(TokenApi tokenApi) {
		this.tokenApi = tokenApi;
	}

	public AssignmentDriver getAssignmentDriver() {
		return assignmentDriver;
	}

	public void setAssignmentDriver(AssignmentDriver assignmentDriver) {
		this.assignmentDriver = assignmentDriver;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public void setAssignmentApi(AssignmentApi assignmentApi) {
		this.assignmentApi = assignmentApi;
	}

}
