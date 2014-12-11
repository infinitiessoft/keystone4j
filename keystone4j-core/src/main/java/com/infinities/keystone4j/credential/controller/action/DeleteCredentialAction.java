package com.infinities.keystone4j.credential.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.credential.Credential;

public class DeleteCredentialAction extends AbstractCredentialAction<Credential> {

	private final String credentialid;


	public DeleteCredentialAction(CredentialApi credentialApi, String credentialid) {
		super(credentialApi);
		this.credentialid = credentialid;
	}

	@Override
	public Credential execute(ContainerRequestContext request) {
		return this.getCredentialApi().deleteCredential(credentialid);
	}

	@Override
	public String getName() {
		return "delete_credential";
	}

}
