package com.infinities.keystone4j.auth.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class RevokeTokenAction extends AbstractAuthAction implements ProtectedAction<TokenDataWrapper> {

	public RevokeTokenAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TokenApi tokenApi) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, tokenApi);
	}

	@Override
	public TokenIdAndData execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		String tokenid = context.getSubjectTokenid();
		this.tokenProviderApi.revokeToken(tokenid);

		return null;
	}

	@Override
	public String getName() {
		return "revoke_token";
	}

}
