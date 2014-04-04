package com.infinities.keystone4j.endpointfilter.driver;

import java.text.MessageFormat;
import java.util.List;

import com.google.common.collect.Lists;
import com.infinities.keystone4j.endpointfilter.EndpointFilterDriver;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.impl.EndpointDao;
import com.infinities.keystone4j.jpa.impl.ProjectDao;
import com.infinities.keystone4j.jpa.impl.ProjectEndpointDao;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.catalog.Endpoint;
import com.infinities.keystone4j.model.endpointfilter.ProjectEndpoint;

public class EndpointFilterJpaDriver implements EndpointFilterDriver {

	private final ProjectEndpointDao projectEndpointDao;
	private final ProjectDao projectDao;
	private final EndpointDao endpointDao;


	public EndpointFilterJpaDriver() {
		projectEndpointDao = new ProjectEndpointDao();
		projectDao = new ProjectDao();
		endpointDao = new EndpointDao();
	}

	@Override
	public void addEndpointToProject(String endpointid, String projectid) {
		ProjectEndpoint projectEndpoint = new ProjectEndpoint();
		Endpoint endpoint = endpointDao.findById(endpointid);
		Project project = projectDao.findById(projectid);
		projectEndpoint.setEndpoint(endpoint);
		projectEndpoint.setProject(project);
		projectEndpointDao.persist(projectEndpoint);
	}

	@Override
	public void removeEndpointToProject(String endpointid, String projectid) {
		ProjectEndpoint projectEndpoint = getProjectEndpoint(endpointid, projectid);
		projectEndpointDao.remove(projectEndpoint);
	}

	private ProjectEndpoint getProjectEndpoint(String endpointid, String projectid) {
		ProjectEndpoint projectEndpoint = projectEndpointDao.findByProjectidAndEndpointid(projectid, endpointid);
		if (projectEndpoint == null) {
			String msg = "Endpoint ${0} not found in project ${1}";
			String message = MessageFormat.format(msg, endpointid, projectid);
			throw Exceptions.NotFoundException.getInstance(message);
		}
		return projectEndpoint;
	}

	@Override
	public void checkEndpointToProject(String endpointid, String projectid) {
		getProjectEndpoint(endpointid, projectid);
	}

	@Override
	public List<Endpoint> listEndpointsForProject(String projectid) {
		List<ProjectEndpoint> projectEndpoints = projectEndpointDao.listEndpointsForProject(projectid);
		List<Endpoint> endpoints = Lists.newArrayList();
		for (ProjectEndpoint projectEndpoint : projectEndpoints) {
			endpoints.add(projectEndpoint.getEndpoint());
		}
		return endpoints;
	}

	@Override
	public List<Project> listProjectsForEndpoint(String endpointid) {
		List<ProjectEndpoint> projectEndpoints = projectEndpointDao.listProjectsForEndpoint(endpointid);
		List<Project> projects = Lists.newArrayList();
		for (ProjectEndpoint projectEndpoint : projectEndpoints) {
			projects.add(projectEndpoint.getProject());
		}
		return projects;
	}

}
