package com.infinities.keystone4j.configuration;

import com.infinities.keystone4j.option.Option;

//ConfigOpts
public interface ConfigOptsManager {

	Authentication getAuthentication();

	void registerOption(Option option);

	void registerOption(Option option, String group);
}
