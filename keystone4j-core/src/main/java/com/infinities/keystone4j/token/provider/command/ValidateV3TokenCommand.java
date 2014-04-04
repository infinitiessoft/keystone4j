package com.infinities.keystone4j.token.provider.command;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.token.TokenData;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.utils.jackson.JsonUtils;

public class ValidateV3TokenCommand extends AbstractTokenProviderCommand<TokenDataWrapper> {

	private final String tokenid;
	private final static Logger logger = LoggerFactory.getLogger(ValidateV3TokenCommand.class);
	private final static String UNEXPECTED_ERROR = "Unexpected error or malformed token determining token expiry: {}";
	private final static String FAILED_TO_VALIDATE_TOKEN = "Failed to validate token";


	public ValidateV3TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid) {
		super(tokenProviderApi, tokenProviderDriver);
		this.tokenid = tokenid;
	}

	@Override
	public TokenDataWrapper execute() {
		// String uniqueid = null;
		// try {
		// uniqueid = Cms.Instance.hashToken(tokenid);
		// } catch (UnsupportedEncodingException | NoSuchAlgorithmException |
		// DecoderException e) {
		// logger.error("unexpected error", e);
		// throw Exceptions.UnexpectedException.getInstance(null);
		// }
		TokenDataWrapper token = this.getTokenProviderDriver().validateV3Token(tokenid);
		logger.debug("validate token uniqueid: {}", tokenid);
		isValidToken(token);
		return token;
	}

	private void isValidToken(TokenDataWrapper token) {
		Date currentTime = new Date();
		try {

			TokenData tokenData = token.getToken();

			Date expiresAt = tokenData.getExpireAt();
			if (expiresAt.after(currentTime)) {
				return;
			}
		} catch (Exception e) {
			String data;
			try {
				data = JsonUtils.toJson(token, Views.AuthenticateForToken.class);
				logger.error(UNEXPECTED_ERROR, data, e);
			} catch (Exception e1) {
				logger.error("Unexpected error or malformed token determining token expiry", e1);
			}

		}
		throw Exceptions.TokenNotFoundException.getInstance(FAILED_TO_VALIDATE_TOKEN);
	}
}
