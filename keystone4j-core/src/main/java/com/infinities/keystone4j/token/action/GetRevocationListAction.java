package com.infinities.keystone4j.token.action;

import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.auth.RevokedWrapper;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.trust.SignedWrapper;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.jackson.JsonUtils;

public class GetRevocationListAction extends AbstractTokenAction<SignedWrapper> {

	private final static Logger logger = LoggerFactory.getLogger(GetRevocationListAction.class);


	public GetRevocationListAction(AssignmentApi assignmentApi, CatalogApi catalogApi, IdentityApi identityApi,
			TokenApi tokenApi, TokenProviderApi tokenProviderApi, TrustApi trustApi) {
		super(assignmentApi, catalogApi, identityApi, tokenApi, tokenProviderApi, trustApi);
	}

	@Override
	public SignedWrapper execute(ContainerRequestContext request) {
		List<Token> tokens = tokenApi.listRevokedTokens();
		RevokedWrapper data = new RevokedWrapper();
		data.setRevoked(tokens);
		try {
			String certfile = Config.Instance.getOpt(Config.Type.signing, "certfile").asText();
			String keyfile = Config.Instance.getOpt(Config.Type.signing, "keyfile").asText();
			String jsonData = JsonUtils.toJson(data);
			String signedText = Cms.Instance.signText(jsonData, certfile, keyfile);
			logger.debug("signed text: {}", signedText);
			SignedWrapper wrapper = new SignedWrapper();
			wrapper.setSigned(signedText);
			return wrapper;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getName() {
		return "revocation_list";
	}
}
