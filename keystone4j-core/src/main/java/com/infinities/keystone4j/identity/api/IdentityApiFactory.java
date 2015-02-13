package com.infinities.keystone4j.identity.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.contrib.revoke.RevokeApi;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdMappingApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;

public class IdentityApiFactory implements Factory<IdentityApi> {

	private final CredentialApi credentialApi;
	private final IdentityDriver identityDriver;
	private final RevokeApi revokeApi;
	private final IdMappingApi idMappingApi;


	@Inject
	public IdentityApiFactory(RevokeApi revokeApi, CredentialApi credentialApi, IdMappingApi idMappingApi,
			IdentityDriver identityDriver) {
		this.credentialApi = credentialApi;
		this.identityDriver = identityDriver;
		this.revokeApi = revokeApi;
		this.idMappingApi = idMappingApi;
	}

	@Override
	public void dispose(IdentityApi arg0) {

	}

	@Override
	public IdentityApi provide() {
		IdentityApi identityApi = new IdentityApiImpl(credentialApi, revokeApi, idMappingApi, identityDriver);
		return identityApi;
	}

}
