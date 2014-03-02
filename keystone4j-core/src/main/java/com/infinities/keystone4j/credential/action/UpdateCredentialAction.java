package com.infinities.keystone4j.credential.action;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.model.Credential;

public class UpdateCredentialAction extends AbstractCredentialAction<Credential> {

	private String credentialid;
	private Credential credential;


	public UpdateCredentialAction(CredentialApi credentialApi, String credentialid, Credential credential) {
		super(credentialApi);
	}

	@Override
	public Credential execute() {
		KeystonePreconditions.requireMatchingId(credentialid, credential);
		return this.getCredentialApi().updateCredential(credentialid, credential);
	}

	@Override
	public String getName() {
		return "update_credential";
	}

}
