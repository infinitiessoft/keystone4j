package com.infinities.keystone4j.credential.command;

import java.util.List;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.model.Credential;

public class ListCredentialsCommand extends AbstractCredentialCommand<List<Credential>> {

	public ListCredentialsCommand(CredentialApi credentialApi, CredentialDriver credentialDriver) {
		super(credentialApi, credentialDriver);
	}

	@Override
	public List<Credential> execute() {
		return this.getCredentialDriver().listCredentials(null);
	}

}
