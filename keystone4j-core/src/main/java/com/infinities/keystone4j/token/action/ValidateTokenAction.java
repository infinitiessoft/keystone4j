package com.infinities.keystone4j.token.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class ValidateTokenAction extends AbstractTokenAction<TokenV2DataWrapper> {

	private final String tokenid;
	private final String belongsTo;


	public ValidateTokenAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenApi tokenApi, TokenProviderApi tokenProviderApi, TrustApi trustApi, String tokenid, String belongsTo) {
		super(assignmentApi, catalogApi, identityApi, tokenApi, tokenProviderApi, trustApi);
		this.belongsTo = belongsTo;
		this.tokenid = tokenid;
	}

	@Override
	public TokenV2DataWrapper execute(ContainerRequestContext request) {
		return this.getTokenProviderApi().validateV2Token(tokenid, belongsTo);
	}

	@Override
	public String getName() {
		return "validate_token";
	}

}
