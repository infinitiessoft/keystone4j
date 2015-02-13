package com.infinities.keystone4j.token.controller.action;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.ProtectedAction;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.policy.PolicyApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;

public class ValidateTokenHeadAction extends AbstractTokenAction implements ProtectedAction<Access> {

	private final String tokenid;


	public ValidateTokenHeadAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenProviderApi tokenProviderApi, TrustApi trustApi, PolicyApi policyApi, String tokenid)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		super(assignmentApi, catalogApi, identityApi, tokenProviderApi, trustApi, policyApi);
		this.tokenid = tokenid;
	}

	@Override
	public MemberWrapper<Access> execute(ContainerRequestContext request) throws Exception {
		String belongsTo = request.getUriInfo().getQueryParameters().getFirst("belongsTo");
		return this.getTokenProviderApi().validateV2Token(tokenid, belongsTo);
	}

	@Override
	public String getName() {
		return "validate_token_head";
	}

	@Override
	public String getCollectionName() {
		return null;
	}

	@Override
	public String getMemberName() {
		return null;
	}

	@Override
	public MemberWrapper<Access> getMemberWrapper() {
		return new TokenV2DataWrapper();
	}

}
