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
