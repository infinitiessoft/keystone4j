package com.infinities.keystone4j.auth.action;

import java.util.List;

import com.infinities.keystone4j.Cms;
import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.auth.model.RevokedWrapper;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.Token;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.model.SignedWrapper;

public class GetRevocationListAction extends AbstractTokenAction<SignedWrapper> {

	private final static String CERT_FILE = "certfile";
	private final static String KEY_FILE = "keyfile";


	public GetRevocationListAction(AssignmentApi assignmentApi, IdentityApi identityApi, TokenProviderApi tokenProviderApi,
			TokenApi tokenApi) {
		super(assignmentApi, identityApi, tokenProviderApi, tokenApi);
	}

	@Override
	public SignedWrapper execute() {
		List<Token> tokens = this.getTokenApi().listRevokedTokens();
		Cms cms = new Cms();

		RevokedWrapper revoked = new RevokedWrapper(tokens);
		String jsonStr = null;
		try {
			jsonStr = JsonUtils.toJson(revoked);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		String signedText = cms.cms_sign_token(jsonStr, Config.Instance.getOpt(Config.Type.signing, CERT_FILE).getText(),
				Config.Instance.getOpt(Config.Type.signing, KEY_FILE).getText());
		return new SignedWrapper(signedText);
	}

}
