package com.infinities.keystone4j.common.model;

import java.util.Set;

public class VersionsWrapper {

	private VersionValueWrapper versions;


	public VersionsWrapper() {

	}

	public VersionsWrapper(Set<Version> version) {
		this.versions = new VersionValueWrapper(version);
	}

	public VersionValueWrapper getVersions() {
		return versions;
	}

	public void setVersions(VersionValueWrapper versions) {
		this.versions = versions;
	}

}
