package com.infinities.keystone4j.credential.action;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.model.Credential;

public class DeleteCredentialAction extends AbstractCredentialAction<Credential> {

	private final String credentialid;


	public DeleteCredentialAction(CredentialApi credentialApi, String credentialid) {
		super(credentialApi);
		this.credentialid = credentialid;
	}

	@Override
	public Credential execute() {
		return this.getCredentialApi().deleteCredential(credentialid);
	}

	@Override
	public String getName() {
		return "delete_credential";
	}

}
