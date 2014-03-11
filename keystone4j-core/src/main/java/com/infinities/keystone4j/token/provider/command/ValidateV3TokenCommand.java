package com.infinities.keystone4j.token.provider.command;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.Cms;
import com.infinities.keystone4j.JsonUtils;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.model.TokenData;
import com.infinities.keystone4j.token.model.TokenDataWrapper;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class ValidateV3TokenCommand extends AbstractTokenProviderCommand<TokenDataWrapper> {

	private final String tokenid;
	private final static Logger logger = LoggerFactory.getLogger(ValidateV3TokenCommand.class);
	private final static String UNEXPECTED_ERROR = "Unexpected error or malformed token determining token expiry: {}";
	private final static String FAILED_TO_VALIDATE_TOKEN = "Failed to validate token";


	public ValidateV3TokenCommand(TokenApi tokenApi, TokenProviderApi tokenProviderApi,
			TokenProviderDriver tokenProviderDriver, String tokenid) {
		super(tokenApi, tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
	}

	@Override
	public TokenDataWrapper execute() {
		String uniqueid = Cms.Instance.hashToken(tokenid);
		TokenDataWrapper token = this.getTokenProviderDriver().validateV3Token(uniqueid);
		isValidToken(token);
		return token;
	}

	private void isValidToken(TokenDataWrapper token) {
		Date currentTime = new Date();
		try {

			TokenData tokenData = token.getToken();

			Date expiresAt = tokenData.getToken().getExpires();
			if (expiresAt.after(currentTime)) {
				return;
			}
		} catch (Exception e) {
			String data;
			try {
				data = JsonUtils.toJson(token);
				logger.error(UNEXPECTED_ERROR, data);
			} catch (Exception e1) {
				logger.error("Unexpected error or malformed token determining token expiry", e1);
			}

		}
		throw Exceptions.TokenNotFoundException.getInstance(FAILED_TO_VALIDATE_TOKEN);
	}
}
