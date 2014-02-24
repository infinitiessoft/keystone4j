package com.infinities.keystone4j.endpointfilter.controller;

import com.infinities.keystone4j.assignment.model.ProjectsWrapper;
import com.infinities.keystone4j.catalog.model.EndpointsWrapper;

public interface EndpointFilterController {

	ProjectsWrapper listProjectsForEndpoint(String endpointid, int page, int perPage);

	void addEndpointToProject(String projectid, String endpointid);

	void checkEndpointInProject(String projectid, String endpointid);

	EndpointsWrapper listEndpointsForProject(String projectid, int page, int perPage);

	void removeEndpointFromProject(String projectid, String endpointid);

}
