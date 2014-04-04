package com.infinities.keystone4j.model.common;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VersionsWrapper {

	private Set<Version> versions;


	public VersionsWrapper() {

	}

	public VersionsWrapper(Set<Version> versions) {
		this.versions = versions;
	}

	public Set<Version> getVersions() {
		return versions;
	}

	public void setVersions(Set<Version> versions) {
		this.versions = versions;
	}

}
