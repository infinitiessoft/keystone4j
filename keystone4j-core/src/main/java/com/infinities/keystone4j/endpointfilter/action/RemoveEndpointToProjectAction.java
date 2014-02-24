package com.infinities.keystone4j.endpointfilter.action;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;

public class RemoveEndpointToProjectAction extends AbstractEndpointFilterAction<Endpoint> {

	private final String endpointid;
	private final String projectid;


	public RemoveEndpointToProjectAction(AssignmentApi assignmentApi, CatalogApi catalogApi,
			EndpointFilterApi endpointFilterApi, String projectid, String endpointid) {
		super(assignmentApi, catalogApi, endpointFilterApi);
		this.projectid = projectid;
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute() {
		this.getEndpointFilterApi().removeEndpointFromProject(endpointid, projectid);
		return null;
	}
}
