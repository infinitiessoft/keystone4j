package com.infinities.keystone4j.credential.controller.impl;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.common.BaseControllerFactory;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.controller.CredentialV3Controller;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;

public class CredentialV3ControllerFactory extends BaseControllerFactory implements Factory<CredentialV3Controller> {

	private final CredentialApi credentialApi;
	private final TokenApi tokenApi;
	private final PolicyApi policyApi;


	@Inject
	public CredentialV3ControllerFactory(CredentialApi credentialApi, TokenApi tokenApi, PolicyApi policyApi) {
		this.credentialApi = credentialApi;
		this.tokenApi = tokenApi;
		this.policyApi = policyApi;
	}

	@Override
	public void dispose(CredentialV3Controller arg0) {

	}

	@Override
	public CredentialV3Controller provide() {
		CredentialV3ControllerImpl controller = new CredentialV3ControllerImpl(credentialApi, tokenApi, policyApi);
		controller.setRequest(getRequest());
		return controller;
	}

}
