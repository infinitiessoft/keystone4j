package com.infinities.keystone4j.token.provider.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.Cms;
import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.exception.UnexpectedException;
import com.infinities.keystone4j.token.model.TokenDataWrapper;

public class PkiProvider extends TokenProviderBaseDriver {

	private final static Logger logger = LoggerFactory.getLogger(PkiProvider.class);


	@Override
	protected String getTokenId(TokenDataWrapper tokenData) {
		try {
			String jsonStr = JsonUtils.toJson(tokenData);
			String tokenid = Cms.Instance.signToken(jsonStr);

			return tokenid;
		} catch (Exception e) {
			logger.error("Unable to sign token");
			throw new UnexpectedException("Unable to sign token.");
		}
	}
}
