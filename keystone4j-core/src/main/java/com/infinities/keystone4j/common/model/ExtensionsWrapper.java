package com.infinities.keystone4j.common.model;

import java.util.Set;

public class ExtensionsWrapper {

	private ExtensionValueWrapper extensions;


	public ExtensionsWrapper() {

	}

	public ExtensionsWrapper(Set<Extension> values) {
		extensions = new ExtensionValueWrapper(values);

	}

	public ExtensionValueWrapper getExtensions() {
		return extensions;
	}

	public void setExtensions(ExtensionValueWrapper extensions) {
		this.extensions = extensions;
	}

}
