package com.infinities.keystone4j.credential.api.command;

import java.util.List;

import com.infinities.keystone4j.TruncatedCommand;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.credential.CredentialDriver;
import com.infinities.keystone4j.model.credential.Credential;

public class ListCredentialsCommand extends AbstractCredentialCommand implements TruncatedCommand<Credential> {

	public ListCredentialsCommand(CredentialDriver credentialDriver) {
		super(credentialDriver);
	}

	@Override
	public List<Credential> execute(Hints hints) throws Exception {
		if (hints == null) {
			hints = new Hints();
		}
		return this.getCredentialDriver().listCredentials(hints);
	}

}
