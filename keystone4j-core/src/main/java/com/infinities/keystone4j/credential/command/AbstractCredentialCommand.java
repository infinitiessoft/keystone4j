package com.infinities.keystone4j.credential.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;

public abstract class AbstractCredentialCommand<T> implements Command<T> {

	private final CredentialApi credentialApi;
	private final CredentialDriver credentialDriver;


	public AbstractCredentialCommand(CredentialApi credentialApi, CredentialDriver credentialDriver) {
		super();
		this.credentialApi = credentialApi;
		this.credentialDriver = credentialDriver;
	}

	public CredentialApi getCredentialApi() {
		return credentialApi;
	}

	public CredentialDriver getCredentialDriver() {
		return credentialDriver;
	}
}
