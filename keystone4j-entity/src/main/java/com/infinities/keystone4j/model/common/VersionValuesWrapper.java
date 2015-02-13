package com.infinities.keystone4j.model.common;

import java.util.Collection;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class VersionValuesWrapper {

	private Collection<Version> values;


	public VersionValuesWrapper() {

	}

	public VersionValuesWrapper(Collection<Version> values) {
		this.values = values;
	}

	public Collection<Version> getValues() {
		return values;
	}

	public void setValues(Collection<Version> values) {
		this.values = values;
	}

}
