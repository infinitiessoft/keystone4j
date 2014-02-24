package com.infinities.keystone4j.credential.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;

public class CredentialApiFactory implements Factory<CredentialApi> {

	private final CredentialDriver catalogDriver;


	@Inject
	public CredentialApiFactory(CredentialDriver catalogDriver) {
		this.catalogDriver = catalogDriver;
	}

	@Override
	public void dispose(CredentialApi arg0) {

	}

	@Override
	public CredentialApi provide() {
		return new CredentialApiImpl(catalogDriver);
	}

}
