package com.infinities.keystone4j.model.common;

import java.util.Set;

public class VersionValueWrapper {

	private Set<Version> values;


	public VersionValueWrapper() {

	}

	public VersionValueWrapper(Set<Version> values) {
		this.values = values;

	}

	public Set<Version> getValues() {
		return values;
	}

	public void setValues(Set<Version> values) {
		this.values = values;
	}

}
