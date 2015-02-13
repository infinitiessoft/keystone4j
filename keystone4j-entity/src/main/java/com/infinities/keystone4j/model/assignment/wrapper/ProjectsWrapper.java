package com.infinities.keystone4j.model.assignment.wrapper;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.common.CollectionLinks;

public class ProjectsWrapper implements CollectionWrapper<Project> {

	private List<Project> projects;
	private boolean truncated;

	private CollectionLinks links = new CollectionLinks();


	public ProjectsWrapper() {

	}

	public ProjectsWrapper(List<Project> projects) {
		this.projects = projects;
	}

	@Override
	public CollectionLinks getLinks() {
		return links;
	}

	@Override
	public void setLinks(CollectionLinks links) {
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

	@XmlElement(name = "projects")
	public List<Project> getRefs() {
		return projects;
	}

}
