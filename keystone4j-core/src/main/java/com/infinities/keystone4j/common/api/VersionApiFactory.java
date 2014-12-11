package com.infinities.keystone4j.common.api;

import org.glassfish.hk2.api.Factory;

public class VersionApiFactory implements Factory<VersionApi> {

	@Override
	public void dispose(VersionApi arg0) {

	}

	@Override
	public VersionApi provide() {
		return new VersionApi("admin");
	}

}
