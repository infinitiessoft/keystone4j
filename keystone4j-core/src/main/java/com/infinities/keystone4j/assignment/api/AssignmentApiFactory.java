package com.infinities.keystone4j.assignment.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.AssignmentDriver;
import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;

public class AssignmentApiFactory implements Factory<AssignmentApi> {

	private final CredentialApi credentialApi;
	private final RevokeApi revokeApi;
	private final AssignmentDriver assignmentDriver;


	@Inject
	public AssignmentApiFactory(CredentialApi credentialApi, RevokeApi revokeApi, AssignmentDriver assignmentDriver) {
		this.credentialApi = credentialApi;
		this.revokeApi = revokeApi;
		this.assignmentDriver = assignmentDriver;
	}

	@Override
	public void dispose(AssignmentApi arg0) {

	}

	@Override
	public AssignmentApi provide() {
		return new AssignmentApiImpl(credentialApi, revokeApi, assignmentDriver);
	}

}
