package com.infinities.keystone4j.credential.api.command;

import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class DeleteCredentialsForUserCommand extends AbstractCredentialCommand<Credential> {

	private final String userid;


	public DeleteCredentialsForUserCommand(CredentialDriver credentialDriver, String userid) {
		super(credentialDriver);
		this.userid = userid;
	}

	@Override
	public Credential execute() {
		this.getCredentialDriver().deleteCredentialsForUser(userid);
		return null;
	}

}
