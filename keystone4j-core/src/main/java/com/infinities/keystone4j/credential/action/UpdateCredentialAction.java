package com.infinities.keystone4j.credential.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystonePreconditions;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.model.Credential;

public class UpdateCredentialAction extends AbstractCredentialAction<Credential> {

	private final String credentialid;
	private final Credential credential;


	public UpdateCredentialAction(CredentialApi credentialApi, String credentialid, Credential credential) {
		super(credentialApi);
		this.credential = credential;
		this.credentialid = credentialid;
	}

	@Override
	public Credential execute(ContainerRequestContext request) {
		KeystonePreconditions.requireMatchingId(credentialid, credential);
		return this.getCredentialApi().updateCredential(credentialid, credential);
	}

	@Override
	public String getName() {
		return "update_credential";
	}

}
