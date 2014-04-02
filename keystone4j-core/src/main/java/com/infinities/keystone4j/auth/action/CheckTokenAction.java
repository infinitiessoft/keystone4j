package com.infinities.keystone4j.auth.action;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.model.TokenMetadata;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CheckTokenAction extends AbstractTokenAction<TokenMetadata> {

	private final static Logger logger = LoggerFactory.getLogger(CheckTokenAction.class);


	public CheckTokenAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TokenApi tokenApi) {
		super(assignmentApi, identityApi, tokenProviderApi, tokenApi);
	}

	@Override
	public TokenMetadata execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		String tokenid = context.getSubjectTokenid();
		logger.debug("check token: {}", tokenid);
		this.tokenProviderApi.checkV3Token(tokenid);
		return null;
	}

	@Override
	public String getName() {
		return "check_token";
	}

}
