package com.infinities.keystone4j.option;

import java.util.List;

import com.google.common.collect.Lists;

public class ListOption extends Option {

	private final List<String> listValue;
	private final String regex;


	public ListOption(String name, String regex, String value) {
		super(name, value);
		this.regex = regex;
		value = offBucket(value);
		listValue = Lists.newArrayList(value.split(regex));
	}

	private String offBucket(String value) {
		if (value.length() < 2) {
			throw new IllegalArgumentException(value + " length < 2.");
		}
		if (value.charAt(0) != '[' || value.charAt(value.length() - 1) != ']') {
			throw new IllegalArgumentException(value + " is not a list.");
		}
		return value.substring(1, value.length() - 1).trim();
	}

	public ListOption(String name, List<String> listValue) {
		super(name, listValue.toString());
		this.regex = ",";
		this.listValue = Lists.newArrayList();
		this.listValue.addAll(listValue);
	}

	@Override
	public List<String> getListValue() {
		return listValue;
	}

	@Override
	public void resetValue(String value) {
		setValue(value);
		value = offBucket(value);
		listValue.clear();
		listValue.addAll(Lists.newArrayList(value.split(regex)));
	}
}
