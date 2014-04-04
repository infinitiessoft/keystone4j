package com.infinities.keystone4j.model.assignment;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ReferentialLinkUtils;
import com.infinities.keystone4j.model.common.Links;

public class ProjectsWrapper {

	private List<Project> projects;
	private Links links = new Links();


	public ProjectsWrapper(List<Project> projects, ContainerRequestContext context) {
		String baseUrl = context.getUriInfo().getBaseUri().toASCIIString() + "v3/projects/";
		this.projects = projects;
		for (Project project : projects) {
			ReferentialLinkUtils.instance.addSelfReferentialLink(project, baseUrl);
		}
		links.setSelf(context.getUriInfo().getRequestUri().toASCIIString());
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Links getLinks() {
		return links;
	}

	public void setLinks(Links links) {
		this.links = links;
	}
}
