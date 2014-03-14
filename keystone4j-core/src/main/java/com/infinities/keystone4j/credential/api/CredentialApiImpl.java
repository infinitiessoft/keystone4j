package com.infinities.keystone4j.credential.api;

import java.util.List;

import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.credential.command.CreateCredentialCommand;
import com.infinities.keystone4j.credential.command.DeleteCredentialCommand;
import com.infinities.keystone4j.credential.command.DeleteCredentialsForProjectCommand;
import com.infinities.keystone4j.credential.command.DeleteCredentialsForUserCommand;
import com.infinities.keystone4j.credential.command.GetCredentialCommand;
import com.infinities.keystone4j.credential.command.ListCredentialsCommand;
import com.infinities.keystone4j.credential.command.UpdateCredentialCommand;
import com.infinities.keystone4j.credential.model.Credential;

public class CredentialApiImpl implements CredentialApi {

	private final CredentialDriver credentialDriver;


	public CredentialApiImpl(CredentialDriver credentialDriver) {
		super();
		this.credentialDriver = credentialDriver;
	}

	@Override
	public Credential createCredential(Credential credential) {
		CreateCredentialCommand command = new CreateCredentialCommand(this, credentialDriver, credential);
		return command.execute();
	}

	@Override
	public List<Credential> listCredentials() {
		ListCredentialsCommand command = new ListCredentialsCommand(this, credentialDriver);
		return command.execute();
	}

	@Override
	public Credential getCredential(String credentialid) {
		GetCredentialCommand command = new GetCredentialCommand(this, credentialDriver, credentialid);
		return command.execute();
	}

	@Override
	public Credential updateCredential(String credentialid, Credential credential) {
		UpdateCredentialCommand command = new UpdateCredentialCommand(this, credentialDriver, credentialid, credential);
		return command.execute();
	}

	@Override
	public Credential deleteCredential(String credentialid) {
		DeleteCredentialCommand command = new DeleteCredentialCommand(this, credentialDriver, credentialid);
		return command.execute();
	}

	@Override
	public void deleteCredentialsForProject(String projectid) {
		DeleteCredentialsForProjectCommand command = new DeleteCredentialsForProjectCommand(this, credentialDriver,
				projectid);
		command.execute();
	}

	@Override
	public void deleteCredentialsForUser(String userid) {
		DeleteCredentialsForUserCommand command = new DeleteCredentialsForUserCommand(this, credentialDriver, userid);
		command.execute();
	}

}
