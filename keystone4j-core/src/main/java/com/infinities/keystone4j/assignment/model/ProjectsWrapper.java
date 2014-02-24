package com.infinities.keystone4j.assignment.model;

import java.util.List;

public class ProjectsWrapper {

	private List<Project> projects;


	public ProjectsWrapper(List<Project> projects) {
		super();
		this.projects = projects;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

}
