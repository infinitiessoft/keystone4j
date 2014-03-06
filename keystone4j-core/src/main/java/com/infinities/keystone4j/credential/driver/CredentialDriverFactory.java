package com.infinities.keystone4j.credential.driver;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.credential.CredentialDriver;

public class CredentialDriverFactory implements Factory<CredentialDriver> {

	public CredentialDriverFactory() {
	}

	@Override
	public void dispose(CredentialDriver arg0) {

	}

	@Override
	public CredentialDriver provide() {
		String driver = Config.Instance.getOpt(Config.Type.credential, "driver").asText();
		try {
			Class<?> c = Class.forName(driver);
			return (CredentialDriver) c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
