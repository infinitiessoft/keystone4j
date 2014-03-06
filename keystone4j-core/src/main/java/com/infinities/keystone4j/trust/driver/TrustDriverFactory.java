package com.infinities.keystone4j.trust.driver;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.trust.TrustDriver;

public class TrustDriverFactory implements Factory<TrustDriver> {

	public TrustDriverFactory() {
	}

	@Override
	public void dispose(TrustDriver arg0) {

	}

	@Override
	public TrustDriver provide() {
		String driver = Config.Instance.getOpt(Config.Type.trust, "driver").asText();
		try {
			Class<?> c = Class.forName(driver);
			return (TrustDriver) c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
