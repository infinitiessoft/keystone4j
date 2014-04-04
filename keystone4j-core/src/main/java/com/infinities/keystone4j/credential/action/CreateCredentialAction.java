package com.infinities.keystone4j.credential.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.credential.Credential;

public class CreateCredentialAction extends AbstractCredentialAction<Credential> {

	private Credential credential;


	public CreateCredentialAction(CredentialApi credentialApi, Credential credential) {
		super(credentialApi);
		this.credential = credential;
	}

	@Override
	public Credential execute(ContainerRequestContext request) {
		credential = assignUniqueId(credential);
		Credential ret = credentialApi.createCredential(credential);
		return ret;
	}

	private Credential assignUniqueId(Credential ref) {
		// TODO not implemented yet
		return ref;
	}

	@Override
	public String getName() {
		return "create_credential";
	}
}
