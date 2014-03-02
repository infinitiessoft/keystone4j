package com.infinities.keystone4j.endpointfilter.action;

import java.util.List;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;

public class ListProjectsForEndpointAction extends AbstractEndpointFilterAction<List<Project>> {

	private final String endpointid;


	public ListProjectsForEndpointAction(AssignmentApi assignmentApi, CatalogApi catalogApi,
			EndpointFilterApi endpointFilterApi, String endpointid) {
		super(assignmentApi, catalogApi, endpointFilterApi);
		this.endpointid = endpointid;
	}

	@Override
	public List<Project> execute() {
		return this.getEndpointFilterApi().listProjectsForEndpoint(endpointid);
	}

	@Override
	public String getName() {
		return "list_projects_for_endpoint";
	}
}
