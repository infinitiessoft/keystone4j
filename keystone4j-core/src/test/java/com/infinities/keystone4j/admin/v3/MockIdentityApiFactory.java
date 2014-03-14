package com.infinities.keystone4j.admin.v3;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.identity.IdentityApi;

public class MockIdentityApiFactory implements Factory<IdentityApi> {

	private final IdentityApi identityApi;


	public MockIdentityApiFactory(IdentityApi identityApi) {
		this.identityApi = identityApi;
	}

	@Override
	public void dispose(IdentityApi arg0) {

	}

	@Override
	public IdentityApi provide() {
		return identityApi;
	}

}
