package com.infinities.keystone4j.credential.api.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class UpdateCredentialCommand extends AbstractCredentialCommand implements NonTruncatedCommand<Credential> {

	private final static Logger logger = LoggerFactory.getLogger(UpdateCredentialCommand.class);
	private final String credentialid;
	private final Credential credential;


	public UpdateCredentialCommand(CredentialDriver credentialDriver, String credentialid, Credential credential) {
		super(credentialDriver);
		this.credentialid = credentialid;
		this.credential = credential;
	}

	@Override
	public Credential execute() throws Exception {
		logger.debug("update credential: {}", credentialid);
		return this.getCredentialDriver().updateCredential(credentialid, credential);
	}

}
