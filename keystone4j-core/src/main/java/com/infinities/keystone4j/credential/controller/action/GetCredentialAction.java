package com.infinities.keystone4j.credential.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class GetCredentialAction extends AbstractCredentialAction implements ProtectedAction<Credential> {

	private final String credentialid;


	public GetCredentialAction(CredentialApi credentialApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi,
			String credentialid) {
		super(credentialApi, tokenProviderApi, policyApi);
		this.credentialid = credentialid;
	}

	@Override
	public MemberWrapper<Credential> execute(ContainerRequestContext request) throws Exception {
		Credential ref = this.getCredentialApi().getCredential(credentialid);
		return this.wrapMember(request, ref);
	}

	@Override
	public String getName() {
		return "get_credential";
	}
}
