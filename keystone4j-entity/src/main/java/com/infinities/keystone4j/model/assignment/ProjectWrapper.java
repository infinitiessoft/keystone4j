package com.infinities.keystone4j.model.assignment;

import com.infinities.keystone4j.model.MemberWrapper;

public class ProjectWrapper implements MemberWrapper<Project> {

	private Project project;


	public ProjectWrapper() {

	}

	public ProjectWrapper(Project project) {
		this.project = project;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(project,
		// baseUrl);
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public void setRef(Project ref) {
		this.project = ref;
	}
}
