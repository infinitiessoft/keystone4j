package com.infinities.keystone4j.identity.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.identity.IdentityDriver;
import com.infinities.keystone4j.token.TokenApi;

public abstract class AbstractIdentityCommand<T> implements Command<T> {

	// private final AssignmentApi assignmentApi;
	private final CredentialApi credentialApi;
	private final TokenApi tokenApi;
	private final IdentityApi identityApi;
	private final IdentityDriver identityDriver;


	public AbstractIdentityCommand(CredentialApi credentialApi, TokenApi tokenApi, IdentityApi identityApi,
			IdentityDriver identityDriver) {
		super();
		this.credentialApi = credentialApi;
		this.tokenApi = tokenApi;
		this.identityApi = identityApi;
		this.identityDriver = identityDriver;
	}

	public CredentialApi getCredentialApi() {
		return credentialApi;
	}

	public TokenApi getTokenApi() {
		return tokenApi;
	}

	public IdentityDriver getIdentityDriver() {
		return identityDriver;
	}

	public IdentityApi getIdentityApi() {
		return identityApi;
	}

}
