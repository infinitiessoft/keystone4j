package com.infinities.keystone4j.credential.command;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.model.Credential;

public class GetCredentialCommand extends AbstractCredentialCommand<Credential> {

	private final String credentialid;


	public GetCredentialCommand(CredentialApi credentialApi, CredentialDriver credentialDriver, String credentialid) {
		super(credentialApi, credentialDriver);
		this.credentialid = credentialid;
	}

	@Override
	public Credential execute() {
		return this.getCredentialDriver().getCredential(credentialid);
	}

}
