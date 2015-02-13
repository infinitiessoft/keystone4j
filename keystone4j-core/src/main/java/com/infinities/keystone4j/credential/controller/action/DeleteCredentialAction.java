package com.infinities.keystone4j.credential.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class DeleteCredentialAction extends AbstractCredentialAction implements ProtectedAction<Credential> {

	private final String credentialid;


	public DeleteCredentialAction(CredentialApi credentialApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String credentialid) {
		super(credentialApi, tokenProviderApi, policyApi);
		this.credentialid = credentialid;
	}

	@Override
	public MemberWrapper<Credential> execute(ContainerRequestContext request) throws Exception {
		this.getCredentialApi().deleteCredential(credentialid);
		return null;
	}

	@Override
	public String getName() {
		return "delete_credential";
	}

}
