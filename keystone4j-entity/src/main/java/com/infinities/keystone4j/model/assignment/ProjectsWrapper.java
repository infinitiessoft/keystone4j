package com.infinities.keystone4j.model.assignment;

import java.util.List;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.common.Links;

public class ProjectsWrapper implements CollectionWrapper<Project> {

	private List<Project> projects;
	private boolean truncated;

	private Links links = new Links();


	public ProjectsWrapper() {

	}

	public ProjectsWrapper(List<Project> projects) {
		this.projects = projects;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	@Override
	public Links getLinks() {
		return links;
	}

	@Override
	public void setLinks(Links links) {
		this.links = links;
	}

	@Override
	public boolean isTruncated() {
		return truncated;
	}

	@Override
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}

	@Override
	public void setRefs(List<Project> refs) {
		this.projects = refs;
	}

}
