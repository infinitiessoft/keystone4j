package com.infinities.keystone4j.auth.model;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.assignment.model.Domain;
import com.infinities.keystone4j.assignment.model.Project;
import com.infinities.keystone4j.trust.model.Token;

public class Scope {

	private Project project;
	private Domain domain;
	@XmlElement(name = "OS-TRUST:trust")
	private Token trust;


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

	public Token getTrust() {
		return trust;
	}

	public void setTrust(Token trust) {
		this.trust = trust;
	}

}
