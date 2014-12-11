package com.infinities.keystone4j.credential.controller.action;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.credential.CredentialApi;

public abstract class AbstractCredentialAction<T> implements ProtectedAction<T> {

	protected CredentialApi credentialApi;


	public AbstractCredentialAction(CredentialApi credentialApi) {
		this.credentialApi = credentialApi;
	}

	public CredentialApi getCredentialApi() {
		return credentialApi;
	}

	public void setCredentialApi(CredentialApi credentialApi) {
		this.credentialApi = credentialApi;
	}

}
