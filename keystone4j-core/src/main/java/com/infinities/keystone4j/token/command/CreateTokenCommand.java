package com.infinities.keystone4j.token.command;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.utils.Cms;

public class CreateTokenCommand extends AbstractTokenCommand<Token> {

	private final static Logger logger = LoggerFactory.getLogger(CreateTokenCommand.class);
	private final Token token;


	public CreateTokenCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, Token token) {
		super(tokenApi, trustApi, tokenDriver);
		this.token = token;
	}

	@Override
	public Token execute() {
		String uniqueid = null;
		// String t = null;
		try {
			logger.debug("hash token");
			// t = token.getId();
			uniqueid = Cms.Instance.hashToken(token.getId(), null);
			logger.debug("set token uniqueid: {}", uniqueid);
			token.setId(uniqueid);

		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | DecoderException e) {
			logger.error("unexpected error", e);
			throw Exceptions.UnexpectedException.getInstance(null);
		}
		token.setValid(true);
		Token ret = this.getTokenDriver().createToken(token);

		// Token test = this.getTokenApi().getToken(t);

		// logger.info("test == null? {}", String.valueOf(test == null));

		return ret;
	}

}
