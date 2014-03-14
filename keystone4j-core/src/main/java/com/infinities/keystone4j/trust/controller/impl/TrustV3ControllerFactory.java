package com.infinities.keystone4j.trust.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.controller.TrustV3Controller;

public class TrustV3ControllerFactory extends BaseControllerFactory implements Factory<TrustV3Controller> {

	private final AssignmentApi assignmentApi;
	private final IdentityApi identityApi;
	private final TrustApi trustApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	@Inject
	public TrustV3ControllerFactory(AssignmentApi assignmentApi, IdentityApi identityApi, TrustApi trustApi,
			TokenApi tokenApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.identityApi = identityApi;
		this.trustApi = trustApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(TrustV3Controller arg0) {

	}

	@Override
	public TrustV3Controller provide() {
		TrustV3ControllerImpl controller = new TrustV3ControllerImpl(assignmentApi, identityApi, trustApi, tokenApi,
				policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
