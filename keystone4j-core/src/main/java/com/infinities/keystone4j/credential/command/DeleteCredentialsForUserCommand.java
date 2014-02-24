package com.infinities.keystone4j.credential.command;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.model.Credential;

public class DeleteCredentialsForUserCommand extends AbstractCredentialCommand<Credential> {

	private final String userid;


	public DeleteCredentialsForUserCommand(CredentialApi credentialApi, CredentialDriver credentialDriver, String userid) {
		super(credentialApi, credentialDriver);
		this.userid = userid;
	}

	@Override
	public Credential execute() {
		this.getCredentialDriver().deleteCredentialsForUser(userid);
		return null;
	}

}
