package com.infinities.keystone4j.contrib.revoke.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.contrib.revoke.driver.RevokeDriver;

public class RevokeApiFactory implements Factory<RevokeApi> {

	private final RevokeDriver driver;


	@Inject
	public RevokeApiFactory(RevokeDriver driver) {
		this.driver = driver;
	}

	@Override
	public void dispose(RevokeApi arg0) {

	}

	@Override
	public RevokeApi provide() {
		return new RevokeApiImpl(driver);
	}

}
