package com.infinities.keystone4j.credential.api.command;

import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class UpdateCredentialCommand extends AbstractCredentialCommand<Credential> {

	private final String credentialid;
	private final Credential credential;


	public UpdateCredentialCommand(CredentialDriver credentialDriver, String credentialid, Credential credential) {
		super(credentialDriver);
		this.credentialid = credentialid;
		this.credential = credential;
	}

	@Override
	public Credential execute() {
		return this.getCredentialDriver().updateCredential(credentialid, credential);
	}

}
