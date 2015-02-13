package com.infinities.keystone4j.credential.api.command;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class CreateCredentialCommand extends AbstractCredentialCommand implements NonTruncatedCommand<Credential> {

	private final String credentialid;
	private final Credential credential;


	public CreateCredentialCommand(CredentialDriver credentialDriver, String credentialid, Credential credential) {
		super(credentialDriver);
		this.credentialid = credentialid;
		this.credential = credential;
	}

	@Override
	public Credential execute() throws Exception {
		return this.getCredentialDriver().createCredential(credentialid, credential);
	}

}
