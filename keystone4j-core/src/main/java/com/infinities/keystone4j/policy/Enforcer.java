package com.infinities.keystone4j.policy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import com.infinities.keystone4j.model.policy.Context;
import com.infinities.keystone4j.policy.check.BaseCheck;

public interface Enforcer {

	public void setRules(Map<String, BaseCheck> rules, boolean overwrite, boolean useConf);

	public void clear();

	public void loadRules(boolean forceReload) throws MalformedURLException, IOException;

	public boolean enforce(Object rule, Map<String, Object> target, Context creds, boolean doRaise, Exception ex)
			throws Exception;

	public Map<String, BaseCheck> getRules();
}
