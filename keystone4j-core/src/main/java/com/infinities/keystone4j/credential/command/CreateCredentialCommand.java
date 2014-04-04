package com.infinities.keystone4j.credential.command;

import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class CreateCredentialCommand extends AbstractCredentialCommand<Credential> {

	private final Credential credential;


	public CreateCredentialCommand(CredentialDriver credentialDriver, Credential credential) {
		super(credentialDriver);
		this.credential = credential;
	}

	@Override
	public Credential execute() {
		return this.getCredentialDriver().createCredential(credential);
	}

}
