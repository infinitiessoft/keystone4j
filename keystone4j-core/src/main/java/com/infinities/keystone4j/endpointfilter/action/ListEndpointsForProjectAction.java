package com.infinities.keystone4j.endpointfilter.action;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.catalog.model.Endpoint;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;

public class ListEndpointsForProjectAction extends AbstractEndpointFilterAction<List<Endpoint>> {

	private final String projectid;


	public ListEndpointsForProjectAction(AssignmentApi assignmentApi, CatalogApi catalogApi,
			EndpointFilterApi endpointFilterApi, String projectid) {
		super(assignmentApi, catalogApi, endpointFilterApi);
		this.projectid = projectid;
	}

	@Override
	public List<Endpoint> execute() {
		this.getAssignmentApi().getProject(projectid);
		return this.getEndpointFilterApi().listEndpointsForProject(projectid);
	}

	@Override
	public String getName() {
		return "list_endpoints_for_project";
	}
}
