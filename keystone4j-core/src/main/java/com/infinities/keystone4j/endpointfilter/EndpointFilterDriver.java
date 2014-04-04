package com.infinities.keystone4j.endpointfilter;

import java.util.List;

import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.catalog.Endpoint;

public interface EndpointFilterDriver {

	void addEndpointToProject(String endpointid, String projectid);

	void removeEndpointToProject(String endpointid, String projectid);

	void checkEndpointToProject(String endpointid, String projectid);

	List<Endpoint> listEndpointsForProject(String projectid);

	List<Project> listProjectsForEndpoint(String endpointid);

}
