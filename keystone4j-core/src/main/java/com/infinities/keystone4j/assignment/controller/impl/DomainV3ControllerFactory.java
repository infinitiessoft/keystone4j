package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.DomainV3Controller;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class DomainV3ControllerFactory implements Factory<DomainV3Controller> {

	private final AssignmentApi assignmentApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	@Inject
	public DomainV3ControllerFactory(AssignmentApi assignmentApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(DomainV3Controller arg0) {

	}

	@Override
	public DomainV3Controller provide() {
		return new DomainV3ControllerImpl(assignmentApi, tokenApi, policyApi);
	}

}
