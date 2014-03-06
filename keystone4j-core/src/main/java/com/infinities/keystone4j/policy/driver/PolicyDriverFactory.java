package com.infinities.keystone4j.policy.driver;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.policy.PolicyDriver;

public class PolicyDriverFactory implements Factory<PolicyDriver> {

	public PolicyDriverFactory() {
	}

	@Override
	public void dispose(PolicyDriver arg0) {

	}

	@Override
	public PolicyDriver provide() {
		String driver = Config.Instance.getOpt(Config.Type.policy, "driver").asText();
		try {
			Class<?> c = Class.forName(driver);
			return (PolicyDriver) c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
