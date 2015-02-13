package com.infinities.keystone4j.model.assignment.wrapper;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.assignment.Project;

public class ProjectWrapper implements MemberWrapper<Project> {

	private Project project;


	public ProjectWrapper() {

	}

	public ProjectWrapper(Project project) {
		this.project = project;
		// ReferentialLinkUtils.instance.addSelfReferentialLink(project,
		// baseUrl);
	}

	@Override
	public void setRef(Project ref) {
		this.project = ref;
	}

	@XmlElement(name = "project")
	@Override
	public Project getRef() {
		return project;
	}
}
