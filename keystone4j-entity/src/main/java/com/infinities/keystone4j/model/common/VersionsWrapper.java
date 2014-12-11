package com.infinities.keystone4j.model.common;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VersionsWrapper {

	private Collection<Version> versions;


	public VersionsWrapper() {

	}

	public VersionsWrapper(Collection<Version> versions) {
		this.versions = versions;
	}

	public Collection<Version> getVersions() {
		return versions;
	}

	public void setVersions(Collection<Version> versions) {
		this.versions = versions;
	}

}
