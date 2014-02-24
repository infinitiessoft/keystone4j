package com.infinities.keystone4j.token.provider.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.Cms;
import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.UnexpectedException;
import com.infinities.keystone4j.token.model.TokenDataWrapper;

public class PkiProvider extends TokenProviderBaseDriver {

	private final static Logger logger = LoggerFactory.getLogger(PkiProvider.class);
	private final static String CERT_FILE = "certfile";
	private final static String KEY_FILE = "keyfile";
	private final static Cms cms = new Cms();


	@Override
	protected String getTokenId(TokenDataWrapper tokenData) {
		try {

			String jsonStr = JsonUtils.toJson(tokenData);

			String tokenid = cms.cms_sign_token(jsonStr, Config.Instance.getOpt(Config.Type.signing, CERT_FILE).getText(),
					Config.Instance.getOpt(Config.Type.signing, KEY_FILE).getText());

			return tokenid;
		} catch (Exception e) {
			logger.error("Unable to sign token");
			throw new UnexpectedException("Unable to sign token.");
		}
	}
}
