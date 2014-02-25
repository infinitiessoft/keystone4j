package com.infinities.keystone4j.extension.model;

import java.util.Set;

public class ExtensionValueWrapper {

	private Set<Extension> values;


	public ExtensionValueWrapper() {
	}

	public ExtensionValueWrapper(Set<Extension> values) {
		this.values = values;
	}

	public Set<Extension> getValues() {
		return values;
	}

	public void setValues(Set<Extension> values) {
		this.values = values;
	}

}
