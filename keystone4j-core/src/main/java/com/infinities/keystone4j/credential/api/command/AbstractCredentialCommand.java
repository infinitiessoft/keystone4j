package com.infinities.keystone4j.credential.api.command;

import com.infinities.keystone4j.credential.CredentialDriver;

public abstract class AbstractCredentialCommand {

	private final CredentialDriver credentialDriver;


	public AbstractCredentialCommand(CredentialDriver credentialDriver) {
		super();
		this.credentialDriver = credentialDriver;
	}

	public CredentialDriver getCredentialDriver() {
		return credentialDriver;
	}
}
