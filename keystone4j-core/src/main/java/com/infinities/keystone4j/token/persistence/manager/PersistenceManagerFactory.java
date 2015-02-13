package com.infinities.keystone4j.token.persistence.manager;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.persistence.PersistenceManager;
import com.infinities.keystone4j.trust.TrustApi;

//keystone.policy.controllers.PolicyV3 20141211

public class PersistenceManagerFactory implements Factory<PersistenceManager> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final TokenDriver tokenDriver;


	@Inject
	public PersistenceManagerFactory(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenDriver tokenDriver) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.tokenDriver = tokenDriver;
	}

	@Override
	public void dispose(PersistenceManager arg0) {

	}

	@Override
	public PersistenceManager provide() {
		return new PersistenceManagerImpl(assignmentApi, identityApi, trustApi, tokenDriver);
	}

}
