package com.infinities.keystone4j.auth.action;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class RevokeTokenAction extends AbstractTokenAction<TokenMetadata> {

	private HttpServletRequest request;


	public RevokeTokenAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TokenApi tokenApi) {
		super(assignmentApi, identityApi, tokenProviderApi, tokenApi);
	}

	@Override
	public TokenMetadata execute() {
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		String tokenid = context.getSubjectTokenid();
		this.tokenProviderApi.revokeToken(tokenid);
		// invalidate cache

		return null;
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
