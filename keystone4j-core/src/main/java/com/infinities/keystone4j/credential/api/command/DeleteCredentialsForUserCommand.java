package com.infinities.keystone4j.credential.api.command;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class DeleteCredentialsForUserCommand extends AbstractCredentialCommand implements NonTruncatedCommand<Credential> {

	private final String userid;


	public DeleteCredentialsForUserCommand(CredentialDriver credentialDriver, String userid) {
		super(credentialDriver);
		this.userid = userid;
	}

	@Override
	public Credential execute() throws Exception {
		this.getCredentialDriver().deleteCredentialsForUser(userid);
		return null;
	}

}
