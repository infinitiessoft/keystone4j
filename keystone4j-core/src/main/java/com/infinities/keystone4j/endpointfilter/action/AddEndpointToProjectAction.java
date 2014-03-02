package com.infinities.keystone4j.endpointfilter.action;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;

public class AddEndpointToProjectAction extends AbstractEndpointFilterAction<Endpoint> {

	private final String endpointid;
	private final String projectid;


	public AddEndpointToProjectAction(AssignmentApi assignmentApi, CatalogApi catalogApi,
			EndpointFilterApi endpointFilterApi, String projectid, String endpointid) {
		super(assignmentApi, catalogApi, endpointFilterApi);
		this.projectid = projectid;
		this.endpointid = endpointid;
	}

	@Override
	public Endpoint execute() {
		this.getCatalogApi().getEndpoint(endpointid);
		this.getAssignmentApi().getProject(projectid);
		this.getEndpointFilterApi().addEndpointToProject(endpointid, projectid);
		return null;
	}

	@Override
	public String getName() {
		return "add_endpoint_to_project";
	}
}
