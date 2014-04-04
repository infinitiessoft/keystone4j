package com.infinities.keystone4j.credential.command;

import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class DeleteCredentialsForProjectCommand extends AbstractCredentialCommand<Credential> {

	private final String projectid;


	public DeleteCredentialsForProjectCommand(CredentialDriver credentialDriver, String projectid) {
		super(credentialDriver);
		this.projectid = projectid;
	}

	@Override
	public Credential execute() {
		this.getCredentialDriver().deleteCredentialsForProject(projectid);
		return null;
	}

}
