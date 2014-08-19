package com.infinities.keystone4j.token.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.KeystoneContext;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.KeystoneUtils;

public class DeleteTokenAction extends AbstractTokenAction<Boolean> {

	// private final static Logger logger =
	// LoggerFactory.getLogger(GetRevocationListAction.class);
	private final String tokenid;
	private final PolicyApi policyApi;


	public DeleteTokenAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi, TokenApi tokenApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi, String tokenid) {
		super(assignmentApi, catalogApi, identityApi, tokenApi, tokenProviderApi, trustApi);
		this.tokenid = tokenid;
		this.policyApi = policyApi;
	}

	@Override
	public Boolean execute(ContainerRequestContext request) {
		KeystoneContext context = (KeystoneContext) request.getProperty(KeystoneContext.CONTEXT_NAME);
		new KeystoneUtils().assertAdmin(policyApi, tokenApi, context);
		this.getTokenApi().deleteToken(tokenid);
		return true;
	}

	@Override
	public String getName() {
		return "delete_token";
	}
}
