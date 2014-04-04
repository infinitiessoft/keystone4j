package com.infinities.keystone4j.token.command;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.Cms;

public class GetTokenCommand extends AbstractTokenCommand<Token> {

	private final String tokenid;
	private final static Logger logger = LoggerFactory.getLogger(GetTokenCommand.class);


	public GetTokenCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, String tokenid) {
		super(tokenApi, trustApi, tokenDriver);
		this.tokenid = tokenid;
	}

	@Override
	public Token execute() {
		if (Strings.isNullOrEmpty(tokenid)) {
			throw Exceptions.TokenNotFoundException.getInstance(null);
		}
		String uniqueid = null;
		try {
			uniqueid = Cms.Instance.hashToken(tokenid);
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | DecoderException e) {
			logger.error("unexpected error", e);
			throw Exceptions.UnexpectedException.getInstance(null);
		}
		Token token = this.getTokenDriver().getToken(uniqueid);
		logger.debug("get token: {}", uniqueid);
		assertValid(token);
		return token;
	}

	private void assertValid(Token token) {
		Date currentTime = new Date();
		Date expires = token.getExpires();

		if (expires == null || currentTime.after(expires)) {
			throw Exceptions.TokenNotFoundException.getInstance(null, token.getId());
		}

	}
}
