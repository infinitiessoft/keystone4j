package com.infinities.keystone4j.credential.command;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.model.Credential;

public class DeleteCredentialCommand extends AbstractCredentialCommand<Credential> {

	private final String credentialid;


	public DeleteCredentialCommand(CredentialApi credentialApi, CredentialDriver credentialDriver, String credentialid) {
		super(credentialApi, credentialDriver);
		this.credentialid = credentialid;
	}

	@Override
	public Credential execute() {
		this.getCredentialDriver().deleteCredential(credentialid);
		return null;
	}

}
