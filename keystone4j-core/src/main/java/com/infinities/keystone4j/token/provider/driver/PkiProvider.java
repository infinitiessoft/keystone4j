package com.infinities.keystone4j.token.provider.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.jackson.JsonUtils;

public class PkiProvider extends TokenProviderBaseDriver {

	private final static Logger logger = LoggerFactory.getLogger(PkiProvider.class);


	public PkiProvider(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi, TokenApi tokenApi,
			TrustApi trustApi) {
		super(identityApi, assignmentApi, catalogApi, tokenApi, trustApi);
	}

	@Override
	public String getTokenId(Object tokenData) {
		try {
			String jsonStr = JsonUtils.toJson(tokenData, Views.AuthenticateForToken.class);

			String certfile = Config.Instance.getOpt(Config.Type.signing, "certfile").asText();
			String keyfile = Config.Instance.getOpt(Config.Type.signing, "keyfile").asText();
			String tokenid = Cms.Instance.signToken(jsonStr, certfile, keyfile);

			return tokenid;
		} catch (Exception e) {
			logger.error("Unable to sign token", e);
			throw Exceptions.UnexpectedException.getInstance("Unable to sign token.");
		}
	}
}
