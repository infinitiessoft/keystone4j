package com.infinities.keystone4j.credential.controller.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.credential.Credential;

public class ListCredentialsAction extends AbstractCredentialAction<List<Credential>> {

	public ListCredentialsAction(CredentialApi credentialApi) {
		super(credentialApi);
	}

	@Override
	public List<Credential> execute(ContainerRequestContext request) {
		return getCredentialApi().listCredentials();
	}

	@Override
	public String getName() {
		return "list_credentials";
	}
}
