/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
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
	public List<String> asList() {
		return listValue;
	}

	@Override
	public void resetValue(String value) {
		setValue(value);
		value = offBucket(value);
		listValue.clear();
		listValue.addAll(Lists.newArrayList(value.split(regex)));
	}

	@Override
	public Option clone() {
		return new ListOption(this.getName(), this.listValue);
	}
}
