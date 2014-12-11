package com.infinities.keystone4j.assignment.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.controller.DomainV3Controller;
import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

//keystone.assignment.controllers.DomainV3 20141209

public class DomainV3ControllerFactory extends BaseControllerFactory implements Factory<DomainV3Controller> {

	private final AssignmentApi assignmentApi;
	private final TokenProviderApi tokenProviderApi;
	private final PolicyApi policyApi;


	@Inject
	public DomainV3ControllerFactory(AssignmentApi assignmentApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.tokenProviderApi = tokenProviderApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(DomainV3Controller arg0) {

	}

	@Override
	public DomainV3Controller provide() {
		DomainV3ControllerImpl controller = new DomainV3ControllerImpl(assignmentApi, tokenProviderApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}
}
