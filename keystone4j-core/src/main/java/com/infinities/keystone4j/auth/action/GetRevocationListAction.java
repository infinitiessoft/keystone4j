package com.infinities.keystone4j.auth.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.auth.RevokedWrapper;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.trust.SignedWrapper;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.jackson.JsonUtils;

public class GetRevocationListAction extends AbstractTokenAction<SignedWrapper> {

	public GetRevocationListAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TokenApi tokenApi) {
		super(assignmentApi, identityApi, tokenProviderApi, tokenApi);
	}

	@Override
	public SignedWrapper execute(ContainerRequestContext request) {
		List<Token> tokens = this.getTokenApi().listRevokedTokens();

		RevokedWrapper revoked = new RevokedWrapper(tokens);
		String jsonStr = null;
		String signedText = null;
		try {
			jsonStr = JsonUtils.toJson(revoked);
			signedText = Cms.Instance.signToken(jsonStr);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new SignedWrapper(signedText);
	}

	@Override
	public String getName() {
		return "revocation_list";
	}

}
