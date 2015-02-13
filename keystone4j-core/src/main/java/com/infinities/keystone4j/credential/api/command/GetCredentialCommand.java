package com.infinities.keystone4j.credential.api.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class GetCredentialCommand extends AbstractCredentialCommand implements NonTruncatedCommand<Credential> {

	private final static Logger logger = LoggerFactory.getLogger(GetCredentialCommand.class);
	private final String credentialid;


	public GetCredentialCommand(CredentialDriver credentialDriver, String credentialid) {
		super(credentialDriver);
		this.credentialid = credentialid;
	}

	@Override
	public Credential execute() throws Exception {
		logger.debug("get credential: {}", credentialid);
		Credential c = this.getCredentialDriver().getCredential(credentialid);
		return c;
	}
}
