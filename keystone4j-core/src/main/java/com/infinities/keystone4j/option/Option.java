package com.infinities.keystone4j.option;

import java.util.List;

public abstract class Option {

	private final String name;
	private String value;


	public Option(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String asText() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int asInteger() {
		throw new IllegalArgumentException("value is not an integer");
	}

	public boolean asBoolean() {
		throw new IllegalArgumentException("value is not a boolean");
	}

	public List<String> asList() {
		throw new IllegalArgumentException("value is not a list");
	}

	public abstract void resetValue(String value);

	@Override
	public abstract Option clone();

}
