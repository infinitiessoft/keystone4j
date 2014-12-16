package com.infinities.keystone4j.auth.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.auth.TokenIdAndData;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class CheckTokenAction extends AbstractAuthAction implements ProtectedAction<TokenDataWrapper> {

	private final static Logger logger = LoggerFactory.getLogger(CheckTokenAction.class);


	public CheckTokenAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, PolicyApi policyApi) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, policyApi);
	}

	@Override
	public TokenIdAndData execute(ContainerRequestContext request) {
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
