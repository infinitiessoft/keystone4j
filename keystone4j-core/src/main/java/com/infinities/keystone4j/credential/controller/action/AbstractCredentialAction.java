package com.infinities.keystone4j.credential.controller.action;

import com.infinities.keystone4j.AbstractAction;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.model.credential.wrapper.CredentialWrapper;
import com.infinities.keystone4j.model.credential.wrapper.CredentialsWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public abstract class AbstractCredentialAction extends AbstractAction<Credential> {

	protected CredentialApi credentialApi;


	public AbstractCredentialAction(CredentialApi credentialApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(tokenProviderApi, policyApi);
		this.credentialApi = credentialApi;
	}

	public CredentialApi getCredentialApi() {
		return credentialApi;
	}

	public void setCredentialApi(CredentialApi credentialApi) {
		this.credentialApi = credentialApi;
	}

	@Override
	public CollectionWrapper<Credential> getCollectionWrapper() {
		return new CredentialsWrapper();
	}

	@Override
	public MemberWrapper<Credential> getMemberWrapper() {
		return new CredentialWrapper();
	}

	@Override
	public String getCollectionName() {
		return "credentials";
	}

	@Override
	public String getMemberName() {
		return "credential";
	}

}
