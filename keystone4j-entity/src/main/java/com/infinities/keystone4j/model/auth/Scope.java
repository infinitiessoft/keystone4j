package com.infinities.keystone4j.model.auth;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.assignment.Domain;
import com.infinities.keystone4j.model.assignment.Project;
import com.infinities.keystone4j.model.trust.Trust;

public class Scope {

	private Project project;
	private Domain domain;
	@XmlElement(name = "OS-TRUST:trust")
	private Trust trust;


	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Trust getTrust() {
		return trust;
	}

	public void setTrust(Trust trust) {
		this.trust = trust;
	}

}
