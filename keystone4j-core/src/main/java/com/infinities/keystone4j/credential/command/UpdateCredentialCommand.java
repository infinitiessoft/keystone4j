package com.infinities.keystone4j.credential.command;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.model.Credential;

public class UpdateCredentialCommand extends AbstractCredentialCommand<Credential> {

	private final String credentialid;
	private final Credential credential;


	public UpdateCredentialCommand(CredentialApi credentialApi, CredentialDriver credentialDriver, String credentialid,
			Credential credential) {
		super(credentialApi, credentialDriver);
		this.credentialid = credentialid;
		this.credential = credential;
	}

	@Override
	public Credential execute() {
		return this.getCredentialDriver().updateCredential(credentialid, credential);
	}

}
