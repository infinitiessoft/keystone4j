package com.infinities.keystone4j.credential.action;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.credential.CredentialApi;

public abstract class AbstractCredentialAction<T> implements Action<T> {

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
