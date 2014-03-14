package com.infinities.keystone4j.auth.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.controller.AuthController;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.TokenDataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class ValidateTokenAction extends AbstractTokenAction<TokenMetadata> {

	public ValidateTokenAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TokenApi tokenApi) {
		super(assignmentApi, identityApi, tokenProviderApi, tokenApi);
	}

	@Override
	public TokenMetadata execute(ContainerRequestContext request) {
		boolean includeCatalog = !request.getUriInfo().getQueryParameters().containsKey(AuthController.NOCATALOG);
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		String tokenid = context.getSubjectTokenid();
		TokenDataWrapper tokenData = this.tokenProviderApi.validateV3Token(tokenid);

		if (!includeCatalog && tokenData.getToken().getCatalog() != null) {
			tokenData.getToken().setCatalog(null);
		}

		return new TokenMetadata(tokenid, tokenData);
	}

	@Override
	public String getName() {
		return "validate_token";
	}

}
