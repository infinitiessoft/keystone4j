package com.infinities.keystone4j.credential.controller.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.FilterProtectedAction;
import com.infinities.keystone4j.common.Hints;
import com.infinities.keystone4j.credential.CredentialApi;
import com.infinities.keystone4j.model.CollectionWrapper;
import com.infinities.keystone4j.model.credential.Credential;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ListCredentialsAction extends AbstractCredentialAction implements FilterProtectedAction<Credential> {

	public ListCredentialsAction(CredentialApi credentialApi, TokenProviderApi tokenProviderApi, PolicyApi policyApi) {
		super(credentialApi, tokenProviderApi, policyApi);
	}

	@Override
	public CollectionWrapper<Credential> execute(ContainerRequestContext request, String... filters) throws Exception {
		Hints hints = buildDriverHints(request, filters);
		List<Credential> refs = this.getCredentialApi().listCredentials(hints);
		CollectionWrapper<Credential> wrapper = wrapCollection(request, refs, hints);
		return wrapper;
	}

	@Override
	public String getName() {
		return "list_credentials";
	}
}
