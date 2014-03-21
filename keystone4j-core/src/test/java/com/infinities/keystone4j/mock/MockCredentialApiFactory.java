package com.infinities.keystone4j.mock;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.credential.CredentialApi;

public class MockCredentialApiFactory implements Factory<CredentialApi> {

	private final CredentialApi credentialApi;


	public MockCredentialApiFactory(CredentialApi credentialApi) {
		this.credentialApi = credentialApi;
	}

	@Override
	public void dispose(CredentialApi arg0) {

	}

	@Override
	public CredentialApi provide() {
		return credentialApi;
	}

}
