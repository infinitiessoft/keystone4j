package com.infinities.keystone4j.auth.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.infinities.keystone4j.Cms;
import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.model.RevokedWrapper;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.model.SignedWrapper;

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
