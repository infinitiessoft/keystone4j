package com.infinities.keystone4j.assignment.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public abstract class AbstractAssignmentCommand<T> implements Command<T> {

	private final CredentialApi credentialApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final AssignmentApi assignmentApi;
	private final AssignmentDriver assignmentDriver;


	public AbstractAssignmentCommand(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentApi assignmentApi, AssignmentDriver assignmentDriver) {
		super();
		this.credentialApi = credentialApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.assignmentApi = assignmentApi;
		this.assignmentDriver = assignmentDriver;
	}

	public CredentialApi getCredentialApi() {
		return credentialApi;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

	public TokenApi getTokenApi() {
		return tokenApi;
	}

	public AssignmentApi getAssignmentApi() {
		return assignmentApi;
	}

	public AssignmentDriver getAssignmentDriver() {
		return assignmentDriver;
	}

}
