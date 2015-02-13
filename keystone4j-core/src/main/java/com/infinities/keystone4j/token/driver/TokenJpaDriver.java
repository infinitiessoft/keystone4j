package com.infinities.keystone4j.token.driver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.base.Strings;
import com.infinities.keystone4j.common.Config;
import com.infinities.keystone4j.exception.Exceptions;
import com.infinities.keystone4j.jpa.impl.MetadataDao;
import com.infinities.keystone4j.jpa.impl.TokenDao;
import com.infinities.keystone4j.model.token.Token;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.token.provider.api.command.AbstractTokenProviderCommand.Data;
import com.infinities.keystone4j.token.provider.driver.BaseProvider;
import com.infinities.keystone4j.utils.jackson.JsonUtils;

public class TokenJpaDriver implements TokenDriver {

	private final TokenDao tokenDao;
	private final MetadataDao metadataDao;
	private final static Logger logger = LoggerFactory.getLogger(TokenJpaDriver.class);


	public TokenJpaDriver() {
		super();
		tokenDao = new TokenDao();
		metadataDao = new MetadataDao();
	}

	@Override
	public Token getToken(String tokenid) throws JsonParseException, JsonMappingException, IOException {
		if (Strings.isNullOrEmpty(tokenid)) {
			throw Exceptions.TokenNotFoundException.getInstance(null, tokenid);
		}
		logger.debug("find token: {}", tokenid);

		Token token = tokenDao.findById(tokenid);
		if (token == null || !token.getValid()) {
			throw Exceptions.TokenNotFoundException.getInstance(null, tokenid);
		}

		logger.debug("token version: {}, data: {}",
				new Object[] { token.getTokenVersion(), new String(token.getTokenDataBytes()) });
		if ("v3.0".equals(token.getTokenVersion())) {
			logger.debug("get v3 token");
			TokenDataWrapper tokenData = JsonUtils.readJson(new String(token.getTokenDataBytes()),
					new TypeReference<TokenDataWrapper>() {
					});
			token.setTokenData(tokenData);
		} else if ("V2".equals(token.getTokenVersion())) {
			logger.debug("get v2 token");
			TokenV2DataWrapper tokenData = JsonUtils.readJson(new String(token.getTokenDataBytes()),
					new TypeReference<TokenV2DataWrapper>() {
					});
			token.setTokenData(tokenData);
		}

		return token;
	}

	@Override
	public Token createToken(String tokenid, Data data) throws JsonGenerationException, JsonMappingException, IOException {
		Token tokenRef = new Token();
		Calendar expires = data.getExpires();
		if (expires == null) {
			expires = BaseProvider.getDefaultExpireTime();
		}
		tokenRef.setExpires(expires);
		tokenRef.setUserId(data.getUser().getId());
		tokenRef.setBind(data.getBind());
		tokenRef.setId(data.getId());
		tokenRef.setMetadata(data.getMetadata());
		tokenRef.setTokenData(data.getTokenData());
		tokenRef.setTokenVersion(data.getTokenVersion());
		tokenRef.setTrustId(data.getTrustid());
		tokenRef.setTenant(data.getTenant());
		tokenRef.setTokenDataBytes(JsonUtils.toJson(data.getTokenData()).getBytes());
		metadataDao.persist(tokenRef.getMetadata());
		tokenDao.persist(tokenRef);

		return tokenRef;
	}

	@Override
	public void deleteToken(String tokenid) throws JsonParseException, JsonMappingException, IOException {
		Token token = getToken(tokenid);
		token.setValid(false);
		tokenDao.merge(token);
		// tokenDao.remove(token);
	}

	@Override
	public void deleteTokens(String userid, String tenantid, String trustid, String consumerid) {
		tokenDao.deleteToken(userid, tenantid, trustid, consumerid);
	}

	@Override
	public List<Token> listRevokeTokens() {
		List<Token> tokens = tokenDao.listRevokedTokens();
		return tokens;
	}

	@Override
	public void flushExpiredTokens() {
		tokenDao.flushExpiredTokens();
	}

	@Override
	public List<String> listTokens(String userId, String tenantId, String trustId, String consumerId) {
		if (!Config.Instance.getOpt(Config.Type.token, "revoke_by_id").asBoolean()) {
			return new ArrayList<String>();
		}
		if (!Strings.isNullOrEmpty(trustId)) {
			return listTokensForTrust(trustId);
		}
		if (!Strings.isNullOrEmpty(consumerId)) {
			return listTokensForConsumer(userId, consumerId);
		} else {
			return listTokensForUser(userId, tenantId);
		}
	}

	private List<String> listTokensForUser(String userId, String tenantId) {
		return tokenDao.listTokensForUser(userId, tenantId);
	}

	private List<String> listTokensForConsumer(String userId, String consumerId) {
		return tokenDao.listTokensForConsumer(userId, consumerId);
	}

	private List<String> listTokensForTrust(String trustId) {
		return tokenDao.listTokensForTrust(trustId);
	}

}
