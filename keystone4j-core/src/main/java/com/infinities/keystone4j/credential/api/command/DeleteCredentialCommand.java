package com.infinities.keystone4j.credential.api.command;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class DeleteCredentialCommand extends AbstractCredentialCommand implements NonTruncatedCommand<Credential> {

	private final String credentialid;


	public DeleteCredentialCommand(CredentialDriver credentialDriver, String credentialid) {
		super(credentialDriver);
		this.credentialid = credentialid;
	}

	@Override
	public Credential execute() throws Exception {
		this.getCredentialDriver().deleteCredential(credentialid);
		return null;
	}

}
