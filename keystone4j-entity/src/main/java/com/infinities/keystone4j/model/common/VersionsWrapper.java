package com.infinities.keystone4j.model.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VersionsWrapper {

	private VersionValuesWrapper versions;


	public VersionsWrapper() {

	}

	public VersionsWrapper(VersionValuesWrapper versions) {
		this.versions = versions;
	}

	public VersionValuesWrapper getVersions() {
		return versions;
	}

	public void setVersions(VersionValuesWrapper versions) {
		this.versions = versions;
	}

}
