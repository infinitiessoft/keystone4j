package com.infinities.keystone4j.token.provider.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.assignment.AssignmentApi;
import com.infinities.keystone4j.catalog.CatalogApi;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.identity.IdentityApi;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.TokenDataWrapper;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.jackson.JsonUtils;
import com.infinities.keystone4j.utils.jackson.Views;

public class PkiProvider extends TokenProviderBaseDriver {

	private final static Logger logger = LoggerFactory.getLogger(PkiProvider.class);


	public PkiProvider(IdentityApi identityApi, AssignmentApi assignmentApi, CatalogApi catalogApi, TokenApi tokenApi) {
		super(identityApi, assignmentApi, catalogApi, tokenApi);
	}

	@Override
	protected String getTokenId(TokenDataWrapper tokenData) {
		try {
			String jsonStr = JsonUtils.toJson(tokenData, Views.AuthenticateForToken.class);
			String tokenid = Cms.Instance.signToken(jsonStr);

			return tokenid;
		} catch (Exception e) {
			logger.error("Unable to sign token", e);
			throw Exceptions.UnexpectedException.getInstance("Unable to sign token.");
		}
	}
}
