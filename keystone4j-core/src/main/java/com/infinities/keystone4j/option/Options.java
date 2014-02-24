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
