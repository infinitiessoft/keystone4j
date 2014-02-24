package com.infinities.keystone4j.credential.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.controller.CredentialV3Controller;

public class CredentialV3ControllerFactory implements Factory<CredentialV3Controller> {

	private final CredentialApi credentialApi;


	@Inject
	public CredentialV3ControllerFactory(CredentialApi credentialApi) {
		this.credentialApi = credentialApi;
	}

	@Override
	public void dispose(CredentialV3Controller arg0) {

	}

	@Override
	public CredentialV3Controller provide() {
		return new CredentialV3ControllerImpl(credentialApi);
	}

}
