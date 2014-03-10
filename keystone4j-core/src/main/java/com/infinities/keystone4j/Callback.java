package com.infinities.keystone4j;

import java.util.Map;

public interface Callback {

	void execute(KeystoneContext context, Action<?> command, Map<String, Object> parMap);
}
