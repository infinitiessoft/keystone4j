package com.infinities.keystone4j.contrib.revoke.driver.impl;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.contrib.revoke.driver.RevokeDriver;

public class RevokeDriverFactory implements Factory<RevokeDriver> {

	public RevokeDriverFactory() {
	}

	@Override
	public void dispose(RevokeDriver arg0) {

	}

	@Override
	public RevokeDriver provide() {
		return new RevokeJpaDriver();
	}

}
