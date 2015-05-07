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

public class Options {

	public static Option newStrOpt(String name, boolean secret, String defaultVal) {
		return new StringOption(name, secret, defaultVal);
	}

	public static Option newStrOpt(String name, String defaultVal) {
		return new StringOption(name, false, defaultVal);
	}

	public static Option newStrOpt(String name) {
		return new StringOption(name, false, "");
	}

	public static Option newIntOpt(String name, int defaultValue) {
		return new IntegerOption(name, defaultValue);
	}

	public static Option newBoolOpt(String name, boolean defaultValue) {
		return new BooleanOption(name, defaultValue);
	}

	public static Option newListOpt(String name, List<String> defaultValue) {
		return new ListOption(name, defaultValue);
	}

	// public static Option newMultiStrOpt(String name, List<String>
	// defaultValue) {
	// return null;
	// }

}
