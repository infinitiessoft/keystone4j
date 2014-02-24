package com.infinities.keystone4j.common;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class BoolOpt extends Opt {

	protected BoolOpt(String name, String dest, String shortName, String defaultValue, boolean positional, String metavar,
			String help, boolean secret, boolean required, String deprecatedName, String deprecatedGroup,
			List<DeprecatedOpt> deprecatedOpts) {
		super(name, dest, shortName, defaultValue, positional, metavar, help, secret, required, deprecatedName, deprecatedGroup,
				deprecatedOpts);
	}

	static {
		Map<String, Boolean> tmp = Maps.newHashMap();
		tmp.put("1", true);
		tmp.put("0", false);
		tmp.put("yes", true);
		tmp.put("no", false);
		tmp.put("true", true);
		tmp.put("false", false);
		tmp.put("on", true);
		tmp.put("off", false);
		final Map<String, Boolean> booleanStates = Collections.unmodifiableMap(tmp);
	}

}
