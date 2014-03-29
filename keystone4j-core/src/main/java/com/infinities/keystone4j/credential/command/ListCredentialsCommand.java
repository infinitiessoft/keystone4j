package com.infinities.keystone4j.credential.command;

import java.util.List;

import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.model.Credential;

public class ListCredentialsCommand extends AbstractCredentialCommand<List<Credential>> {

	public ListCredentialsCommand(CredentialDriver credentialDriver) {
		super(credentialDriver);
	}

	@Override
	public List<Credential> execute() {
		return this.getCredentialDriver().listCredentials(null);
	}

}
