package com.infinities.keystone4j.credential.api.command;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class DeleteCredentialsForProjectCommand extends AbstractCredentialCommand implements NonTruncatedCommand<Credential> {

	private final String projectid;


	public DeleteCredentialsForProjectCommand(CredentialDriver credentialDriver, String projectid) {
		super(credentialDriver);
		this.projectid = projectid;
	}

	@Override
	public Credential execute() throws Exception {
		this.getCredentialDriver().deleteCredentialsForProject(projectid);
		return null;
	}

}
