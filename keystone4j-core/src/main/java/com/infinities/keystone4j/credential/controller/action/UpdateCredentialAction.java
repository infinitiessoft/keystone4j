package com.infinities.keystone4j.credential.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class UpdateCredentialAction extends AbstractCredentialAction implements ProtectedAction<Credential> {

	private final String credentialid;
	private final Credential credential;


	public UpdateCredentialAction(CredentialApi credentialApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String credentialid, Credential credential) {
		super(credentialApi, tokenProviderApi, policyApi);
		this.credential = credential;
		this.credentialid = credentialid;
	}

	@Override
	public MemberWrapper<Credential> execute(ContainerRequestContext request) throws Exception {
		requireMatchingId(credentialid, credential);
		Credential ref = this.getCredentialApi().updateCredential(credentialid, credential);
		return this.wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "update_credential";
	}

}
