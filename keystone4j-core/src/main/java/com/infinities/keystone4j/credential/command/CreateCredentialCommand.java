package com.infinities.keystone4j.credential.command;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.model.Credential;

public class CreateCredentialCommand extends AbstractCredentialCommand<Credential> {

	private final Credential credential;


	public CreateCredentialCommand(CredentialApi credentialApi, CredentialDriver credentialDriver, Credential credential) {
		super(credentialApi, credentialDriver);
		this.credential = credential;
	}

	@Override
	public Credential execute() {
		return this.getCredentialDriver().createCredential(credential);
	}

}
