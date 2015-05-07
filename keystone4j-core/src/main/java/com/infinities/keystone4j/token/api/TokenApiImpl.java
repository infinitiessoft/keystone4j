/*******************************************************************************
 * # Copyright 2015 InfinitiesSoft Solutions Inc.
 * #
 * # Licensed under the Apache License, Version 2.0 (the "License"); you may
 * # not use this file except in compliance with the License. You may obtain
 * # a copy of the License at
 * #
 * #      http://www.apache.org/licenses/LICENSE-2.0
 * #
 * # Unless required by applicable law or agreed to in writing, software
 * # distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * # WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * # License for the specific language governing permissions and limitations
 * # under the License.
 *******************************************************************************/
//package com.infinities.keystone4j.token.api;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.infinities.keystone4j.model.token.Token;
//import com.infinities.keystone4j.token.TokenApi;
//import com.infinities.keystone4j.token.TokenDriver;
//import com.infinities.keystone4j.token.api.command.CreateTokenCommand;
//import com.infinities.keystone4j.token.api.command.DeleteTokenCommand;
//import com.infinities.keystone4j.token.api.command.DeleteTokensForTrustCommand;
//import com.infinities.keystone4j.token.api.command.DeleteTokensForUserCommand;
//import com.infinities.keystone4j.token.api.command.GetTokenCommand;
//import com.infinities.keystone4j.token.api.command.ListRevokedTokensCommand;
//import com.infinities.keystone4j.trust.TrustApi;
//
//public class TokenApiImpl implements TokenApi {
//
//	private final TrustApi trustApi;
//	private final TokenDriver tokenDriver;
//	private final static Logger logger = LoggerFactory.getLogger(TokenApiImpl.class);
//
//
//	public TokenApiImpl(TrustApi trustApi, TokenDriver tokenDriver) {
//		super();
//		this.trustApi = trustApi;
//		this.tokenDriver = tokenDriver;
//	}
//
//	@Override
//	public Token getToken(String tokenid) {
//		GetTokenCommand command = new GetTokenCommand(this, trustApi, tokenDriver, tokenid);
//		return command.execute();
//	}
//
//	@Override
//	public List<Token> listRevokedTokens() {
//		ListRevokedTokensCommand command = new ListRevokedTokensCommand(this, trustApi, tokenDriver);
//		return command.execute();
//	}
//
//	@Override
//	public void deleteTokensForTrust(String userid, String trustid) {
//		DeleteTokensForTrustCommand command = new DeleteTokensForTrustCommand(this, trustApi, tokenDriver, userid, trustid);
//		command.execute();
//	}
//
//	@Override
//	public void deleteTokensForUser(String userid, String projectid) {
//		DeleteTokensForUserCommand command = new DeleteTokensForUserCommand(this, trustApi, tokenDriver, userid, projectid);
//		command.execute();
//
//	}
//
//	// @Override
//	// public void deleteTokensForDomain(String domainid) {
//	// DeleteTokensForDomainCommand command = new
//	// DeleteTokensForDomainCommand(domainid);
//	// command.execute();
//	// }
//
//	@Override
//	public Token createToken(Token token) {
//		logger.debug("create token");
//		CreateTokenCommand command = new CreateTokenCommand(this, trustApi, tokenDriver, token);
//		return command.execute();
//	}
//
//	@Override
//	public void deleteToken(String tokenid) {
//		DeleteTokenCommand command = new DeleteTokenCommand(this, trustApi, tokenDriver, tokenid);
//		command.execute();
//	}
//
// }
