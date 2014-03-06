package com.infinities.keystone4j.identity.driver;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityDriver;

public class IdentityDriverFactory implements Factory<IdentityDriver> {

	public IdentityDriverFactory() {
	}

	@Override
	public void dispose(IdentityDriver arg0) {

	}

	@Override
	public IdentityDriver provide() {
		String driver = Config.Instance.getOpt(Config.Type.identity, "driver").asText();
		try {
			Class<?> c = Class.forName(driver);
			return (IdentityDriver) c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
