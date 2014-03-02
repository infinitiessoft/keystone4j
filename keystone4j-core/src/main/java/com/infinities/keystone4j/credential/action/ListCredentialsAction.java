package com.infinities.keystone4j.credential.action;

import java.util.List;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.model.Credential;

public class ListCredentialsAction extends AbstractCredentialAction<List<Credential>> {

	public ListCredentialsAction(CredentialApi credentialApi) {
		super(credentialApi);
	}

	@Override
	public List<Credential> execute() {
		return getCredentialApi().listCredentials();
	}

	@Override
	public String getName() {
		return "list_credentials";
	}
}
