package com.infinities.keystone4j.credential.controller.impl;

import java.util.List;

import com.infinities.keystone4j.Action;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.credential.action.CreateCredentialAction;
import com.infinities.keystone4j.credential.action.DeleteCredentialAction;
import com.infinities.keystone4j.credential.action.GetCredentialAction;
import com.infinities.keystone4j.credential.action.ListCredentialsAction;
import com.infinities.keystone4j.credential.action.UpdateCredentialAction;
import com.infinities.keystone4j.credential.controller.CredentialV3Controller;
import com.infinities.keystone4j.credential.model.Credential;
import com.infinities.keystone4j.credential.model.CredentialWrapper;
import com.infinities.keystone4j.credential.model.CredentialsWrapper;
import com.infinities.keystone4j.decorator.FilterCheckDecorator;
import com.infinities.keystone4j.decorator.PaginateDecorator;
import com.infinities.keystone4j.decorator.PolicyCheckDecorator;

public class CredentialV3ControllerImpl implements CredentialV3Controller {

	private final CredentialApi credentialApi;


	public CredentialV3ControllerImpl(CredentialApi credentialApi) {
		this.credentialApi = credentialApi;
	}

	@Override
	public CredentialWrapper createCredential(Credential credential) {
		Action<Credential> command = new PolicyCheckDecorator<Credential>(new CreateCredentialAction(credentialApi,
				credential));
		Credential ret = command.execute();
		return new CredentialWrapper(ret);
	}

	@Override
	public CredentialsWrapper listCredentials(int page, int perPage) {
		Action<List<Credential>> command = new FilterCheckDecorator<List<Credential>>(new PaginateDecorator<Credential>(
				new ListCredentialsAction(credentialApi), page, perPage));

		List<Credential> ret = command.execute();
		return new CredentialsWrapper(ret);
	}

	@Override
	public CredentialWrapper getCredential(String credentialid) {
		Action<Credential> command = new PolicyCheckDecorator<Credential>(new GetCredentialAction(credentialApi,
				credentialid));
		Credential ret = command.execute();
		return new CredentialWrapper(ret);
	}

	@Override
	public CredentialWrapper updateCredential(String credentialid, Credential credential) {
		Action<Credential> command = new PolicyCheckDecorator<Credential>(new UpdateCredentialAction(credentialApi,
				credentialid, credential));
		Credential ret = command.execute();
		return new CredentialWrapper(ret);
	}

	@Override
	public void deleteCredential(String credentialid) {
		Action<Credential> command = new PolicyCheckDecorator<Credential>(new DeleteCredentialAction(credentialApi,
				credentialid));
		command.execute();
	}

}
