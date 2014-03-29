package com.infinities.keystone4j.token.driver;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.token.TokenDriver;

public class TokenDriverFactory implements Factory<TokenDriver> {

	public TokenDriverFactory() {
	}

	@Override
	public void dispose(TokenDriver arg0) {

	}

	@Override
	public TokenDriver provide() {
		String driver = Config.Instance.getOpt(Config.Type.token, "driver").asText();
		try {
			Class<?> c = Class.forName(driver);
			return (TokenDriver) c.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
