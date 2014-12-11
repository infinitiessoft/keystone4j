package com.infinities.keystone4j.endpointfilter.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.endpointfilter.EndpointFilterApi;
import com.infinities.keystone4j.model.catalog.Endpoint;

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
	public Endpoint execute(ContainerRequestContext request) {
		this.getEndpointFilterApi().removeEndpointFromProject(endpointid, projectid);
		return null;
	}

	@Override
	public String getName() {
		return "remove_endpoint_from_project";
	}
}
