package com.infinities.keystone4j.trust.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.controller.TrustV3Controller;

public class TrustV3ControllerFactory implements Factory<TrustV3Controller> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final TokenApi tokenApi;


	@Inject
	public TrustV3ControllerFactory(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenApi tokenApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.tokenApi = tokenApi;
	}

	@Override
	public void dispose(TrustV3Controller arg0) {

	}

	@Override
	public TrustV3Controller provide() {
		return new TrustV3ControllerImpl(assignmentApi, identityApi, trustApi, tokenApi);
	}

}
