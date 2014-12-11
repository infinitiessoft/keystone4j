package com.infinities.keystone4j.token.provider.api.command;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.common.Config.Type;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.token.IToken;
import com.infinities.keystone4j.model.token.TokenData;
import com.infinities.keystone4j.model.token.TokenDataWrapper;
import com.infinities.keystone4j.model.token.v2.Access;
import com.infinities.keystone4j.model.token.v2.TokenV2;
import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
import com.infinities.keystone4j.model.utils.Views;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;
import com.infinities.keystone4j.utils.Cms;
import com.infinities.keystone4j.utils.jackson.JsonUtils;

public abstract class AbstractTokenProviderCommand<T> implements Command<T> {

	private final static Logger logger = LoggerFactory.getLogger(AbstractTokenProviderCommand.class);
	private final TokenProviderApi tokenProviderApi;
	private final TokenProviderDriver tokenProviderDriver;
	private final static String UNEXPECTED_ERROR = "Unexpected error or malformed token determining token expiry: {}";
	private final static String FAILED_TO_VALIDATE_TOKEN = "Failed to validate token";
	private final String V2 = KeystoneToken.V2;
	private final String V3 = KeystoneToken.V3;


	public AbstractTokenProviderCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver) {
		super();
		this.tokenProviderApi = tokenProviderApi;
		this.tokenProviderDriver = tokenProviderDriver;
	}

	public TokenProviderApi getTokenProviderApi() {
		return tokenProviderApi;
	}

	public TokenProviderDriver getTokenProviderDriver() {
		return tokenProviderDriver;
	}

	public String getUniqueId(String tokenid) {
		String uniqueid = null;
		try {
			uniqueid = Cms.Instance.hashToken(tokenid, null);
			return uniqueid;
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | DecoderException e) {
			throw Exceptions.UnexpectedException.getInstance(e);
		}
	}

	public void isValidToken(TokenDataWrapper token) {
		Date currentTime = new Date();
		try {

			TokenData tokenData = token.getToken();

			Calendar expiresAt = tokenData.getExpireAt();
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

	public void isValidToken(IToken token) {
		Calendar currentTime = Calendar.getInstance();
		Calendar expiresAt = null;
		try {
			if (token instanceof TokenV2DataWrapper) {
				expiresAt = ((TokenV2DataWrapper) token).getAccess().getToken().getExpires();
			} else if (token instanceof TokenDataWrapper) {
				expiresAt = ((TokenDataWrapper) token).getToken().getExpireAt();
			}

		} catch (Exception e) {
			logger.error(UNEXPECTED_ERROR, token, e);
			throw Exceptions.TokenNotFoundException.getInstance(FAILED_TO_VALIDATE_TOKEN);
		}
		if (expiresAt.after(currentTime)) {
			checkRevocation(token);
			return;
		} else {
			throw Exceptions.TokenNotFoundException.getInstance(FAILED_TO_VALIDATE_TOKEN);
		}
	}

	protected void checkRevocation(IToken token) {
		String version = this.getTokenProviderDriver().getTokenVersion(tokenRef);
		if (V3.equals(version)) {
			return checkRevocationV3(token);
		} else if (V2.equals(version)) {
			return checkRevocationV2(token);
		}
	}

	protected void checkRevocationV2(IToken token) {
		Access tokenData = null;
		try {
			tokenData = ((TokenV2DataWrapper) token).getAccess();
		} catch (Exception e) {
			throw Exceptions.TokenNotFoundException.getInstance(FAILED_TO_VALIDATE_TOKEN);
		}

		tokenValues = this.getRevokeApi().buildTokenValuesV2(tokenData,
				Config.Instance.getOpt(Type.DEFAULT, "default_domain_id").asText());
		this.getRevokeApi().checkToken(tokenValues);
	}

	protected void checkRevocationV3(IToken token) {
		TokenData tokenData = null;
		try {
			tokenData = ((TokenDataWrapper) token).getToken();
		} catch (Exception e) {
			throw Exceptions.TokenNotFoundException.getInstance(FAILED_TO_VALIDATE_TOKEN);
		}

		tokenValues = this.getRevokeApi().buildTokenValues(tokenData);
		this.getRevokeApi().checkToken(tokenValues);
	}

	protected IToken validateToken(String tokenid) {
		IToken tokenRef = persistence.getToken(tokenid);
		String version = this.getTokenProviderDriver().getTokenVersion(tokenRef);
		if (V3.equals(version)) {
			return this.getTokenProviderDriver().validateV3Token(tokenRef);
		} else if (V2.equals(version)) {
			return this.getTokenProviderDriver().validateV2Token(tokenRef);
		}

		throw Exceptions.UnsupportedTokenVersionException.getInstance();
	}

	public void isValidToken(TokenV2DataWrapper token) {
		Calendar currentTime = Calendar.getInstance();
		try {

			Access tokenData = token.getAccess();

			Calendar expiresAt = tokenData.getToken().getExpires();
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

	public void tokenBelongsTo(TokenV2DataWrapper token, String belongsTo) {
		if (!Strings.isNullOrEmpty(belongsTo)) {
			TokenV2 tokenData = token.getAccess().getToken();
			if (tokenData.getTenant() == null || !belongsTo.equals(tokenData.getTenant().getId())) {
				throw Exceptions.UnauthorizedException.getInstance();
			}
		}
	}

	public void tokenBelongsTo(IToken token, String belongsTo) {
		if (!Strings.isNullOrEmpty(belongsTo)) {
			TokenV2 tokenData = ((TokenV2DataWrapper) token).getAccess().getToken();
			if (tokenData.getTenant() == null || !belongsTo.equals(tokenData.getTenant().getId())) {
				throw Exceptions.UnauthorizedException.getInstance();
			}
		}
	}
}
