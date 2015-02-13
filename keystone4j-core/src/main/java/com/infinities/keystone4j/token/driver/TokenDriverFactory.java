package com.infinities.keystone4j.token.driver;

import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.token.TokenDriver;

public class TokenDriverFactory implements Factory<TokenDriver> {

	private final static Logger logger = LoggerFactory.getLogger(TokenDriverFactory.class);


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
			logger.error("reflect driver failed", e);
			throw new RuntimeException(e);
		}
	}
}
