package com.infinities.keystone4j.assignment.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;

public class AssignmentApiFactory implements Factory<AssignmentApi> {

	private final CredentialApi credentialApi;
	private final IdentityApi identityApi;
	private final TokenApi tokenApi;
	private final AssignmentDriver assignmentDriver;


	@Inject
	public AssignmentApiFactory(CredentialApi credentialApi, IdentityApi identityApi, TokenApi tokenApi,
			AssignmentDriver assignmentDriver) {
		this.credentialApi = credentialApi;
		this.identityApi = identityApi;
		this.tokenApi = tokenApi;
		this.assignmentDriver = assignmentDriver;
	}

	@Override
	public void dispose(AssignmentApi arg0) {

	}

	@Override
	public AssignmentApi provide() {
		return new AssignmentApiImpl(credentialApi, identityApi, tokenApi, assignmentDriver);
	}

}
