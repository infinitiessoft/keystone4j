package com.infinities.keystone4j.token.driver;

import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.impl.TokenDao;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.model.Token;

public class TokenJpaDriver implements TokenDriver {

	private final TokenDao tokenDao;


	public TokenJpaDriver() {
		super();
		tokenDao = new TokenDao();
	}

	@Override
	public Token getToken(String uniqueid) {
		if (Strings.isNullOrEmpty(uniqueid)) {
			throw Exceptions.TokenNotFoundException.getInstance(null, uniqueid);
		}
		Token token = tokenDao.findById(uniqueid);
		if (token == null) {
			throw Exceptions.TokenNotFoundException.getInstance(null, uniqueid);
		}
		return token;
	}

	@Override
	public void deleteToken(String tokenid) {
		Token token = getToken(tokenid);
		token.setValid(false);
		tokenDao.merge(token);
		// tokenDao.remove(token);
	}

	@Override
	public List<Token> listRevokeTokens() {
		List<Token> tokens = tokenDao.findBeforeExpireAndValid(new Date(), false);
		return tokens;
	}

	@Override
	public void flushExpiredTokens() {
		tokenDao.flushExpiredTokens();
	}

	@Override
	public void deleteTokensForTrust(String userid, String trustid) {
		tokenDao.deleteTokensForTrust(userid, trustid);

	}

	@Override
	public void deleteTokensForUser(String userid, String projectid) {
		tokenDao.deleteTokensForUser(userid, projectid);
	}

	@Override
	public Token createToken(Token token) {
		tokenDao.persist(token);
		return token;
	}

}
