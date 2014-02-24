package com.infinities.keystone4j.common;

import java.util.List;
import java.util.Map;

public class StrOpt extends Opt {

	private List<String> choices;

	protected StrOpt(String name, List<String> choices, String dest, String shortName, String defaultValue,
			boolean positional, String metavar, String help, boolean secret, boolean required, String deprecatedName,
			String deprecatedGroup, List<DeprecatedOpt> deprecatedOpts) {
		super(name, dest, shortName, defaultValue, positional, metavar, help, secret, required, deprecatedName,
				deprecatedGroup, deprecatedOpts);
		this.choices = choices;
	}

	@Override
	public Map<String, Object> getArgumentParserKwargs(OptGroup group, Map<String, Object> kwargs) {
		kwargs.put("choices", choices);

		return super.getArgumentParserKwargs(group, kwargs);
	}

	public void validateValue(String value) {
		if (choices != null && !choices.contains(value)) {
			throw new IllegalArgumentException("Invalid value: " + value);
		}
	}

}
