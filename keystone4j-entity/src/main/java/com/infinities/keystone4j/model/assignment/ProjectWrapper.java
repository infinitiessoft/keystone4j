package com.infinities.keystone4j.model.assignment;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;

public class ProjectWrapper {

	private Project project;


	public ProjectWrapper(Project project, ContainerRequestContext context) {
		this(project, context.getUriInfo().getBaseUri().toASCIIString() + "v3/projects/");
	}

	public ProjectWrapper() {

	}

	public ProjectWrapper(Project project, String baseUrl) {
		this.project = project;
		ReferentialLinkUtils.instance.addSelfReferentialLink(project, baseUrl);
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
