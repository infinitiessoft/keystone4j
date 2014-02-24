package com.infinities.keystone4j.auth.action;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.TokenDataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ValidateTokenAction extends AbstractTokenAction<TokenMetadata> {

	private HttpServletRequest request;


	public ValidateTokenAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TokenApi tokenApi) {
		super(assignmentApi, identityApi, tokenProviderApi, tokenApi);
	}

	@Override
	public TokenMetadata execute() {
		boolean includeCatalog = !request.getQueryString().contains(AuthController.NOCATALOG);
		KeystoneContext context = (KeystoneContext) request.getAttribute(KeystoneContext.CONTEXT_NAME);
		String tokenid = context.getTokenid();
		TokenDataWrapper tokenData = this.tokenProviderApi.validateV3Token(tokenid);

		if (!includeCatalog && tokenData.getToken().getCatalog() != null) {
			tokenData.getToken().setCatalog(null);
		}

		return new TokenMetadata(tokenid, tokenData);
	}

	@Context
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
