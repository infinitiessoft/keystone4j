package com.infinities.keystone4j.credential.api.command;

import java.util.List;

import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class ListCredentialsCommand extends AbstractCredentialCommand<List<Credential>> {

	public ListCredentialsCommand(CredentialDriver credentialDriver) {
		super(credentialDriver);
	}

	@Override
	public List<Credential> execute() {
		return this.getCredentialDriver().listCredentials(null);
	}

}
