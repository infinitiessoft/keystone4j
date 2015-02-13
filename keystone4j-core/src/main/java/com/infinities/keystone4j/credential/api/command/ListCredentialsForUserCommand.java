package com.infinities.keystone4j.credential.api.command;

import java.util.List;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class ListCredentialsForUserCommand extends AbstractCredentialCommand implements
		NonTruncatedCommand<List<Credential>> {

	private final String userid;


	public ListCredentialsForUserCommand(CredentialDriver credentialDriver, String userid) {
		super(credentialDriver);
		this.userid = userid;
	}

	@Override
	public List<Credential> execute() throws Exception {
		return this.getCredentialDriver().listCredentialsForUser(userid);
	}

}
