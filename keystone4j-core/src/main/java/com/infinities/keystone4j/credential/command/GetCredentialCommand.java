package com.infinities.keystone4j.credential.command;

import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class GetCredentialCommand extends AbstractCredentialCommand<Credential> {

	private final String credentialid;


	public GetCredentialCommand(CredentialDriver credentialDriver, String credentialid) {
		super(credentialDriver);
		this.credentialid = credentialid;
	}

	@Override
	public Credential execute() {
		return this.getCredentialDriver().getCredential(credentialid);
	}

}
