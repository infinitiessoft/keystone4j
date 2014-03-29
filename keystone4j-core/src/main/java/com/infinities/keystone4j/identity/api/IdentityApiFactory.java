package com.infinities.keystone4j.identity.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.token.TokenApi;

public class IdentityApiFactory implements Factory<IdentityApi> {

	private final CredentialApi credentialApi;
	private final TokenApi tokenApi;
	private final IdentityDriver identityDriver;


	@Inject
	public IdentityApiFactory(CredentialApi credentialApi, TokenApi tokenApi, IdentityDriver identityDriver) {
		this.credentialApi = credentialApi;
		this.tokenApi = tokenApi;
		this.identityDriver = identityDriver;
	}

	@Override
	public void dispose(IdentityApi arg0) {

	}

	@Override
	public IdentityApi provide() {
		IdentityApi identityApi = new IdentityApiImpl(credentialApi, tokenApi, identityDriver);
		return identityApi;
	}

}
