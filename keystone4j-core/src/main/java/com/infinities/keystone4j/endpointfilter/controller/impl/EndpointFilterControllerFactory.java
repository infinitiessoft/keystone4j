package com.infinities.keystone4j.endpointfilter.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
import com.infinities.keystone4j.endpointfilter.controller.EndpointFilterController;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class EndpointFilterControllerFactory implements Factory<EndpointFilterController> {

	private final AssignmentApi assignmentApi;
	private final CatalogApi catalogApi;
	private final EndpointFilterApi endpointFilterApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	@Inject
	public EndpointFilterControllerFactory(AssignmentApi assignmentApi, CatalogApi catalogApi,
			EndpointFilterApi endpointFilterApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.assignmentApi = assignmentApi;
		this.catalogApi = catalogApi;
		this.endpointFilterApi = endpointFilterApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(EndpointFilterController arg0) {

	}

	@Override
	public EndpointFilterController provide() {
		return new EndpointFilterControllerImpl(assignmentApi, catalogApi, endpointFilterApi, tokenApi, policyApi);
	}

}
