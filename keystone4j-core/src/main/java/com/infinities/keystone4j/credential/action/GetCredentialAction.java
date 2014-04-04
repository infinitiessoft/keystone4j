package com.infinities.keystone4j.credential.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.credential.Credential;

public class GetCredentialAction extends AbstractCredentialAction<Credential> {

	private final String credentialid;


	public GetCredentialAction(CredentialApi credentialApi, String credentialid) {
		super(credentialApi);
		this.credentialid = credentialid;
	}

	@Override
	public Credential execute(ContainerRequestContext request) {
		return this.getCredentialApi().getCredential(credentialid);
	}

	@Override
	public String getName() {
		return "get_credential";
	}
}
