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
//package com.infinities.keystone4j.token.api.command;
//
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//
//import org.apache.commons.codec.DecoderException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.infinities.keystone4j.exception.Exceptions;
//import com.infinities.keystone4j.model.token.Token;
//import com.infinities.keystone4j.token.TokenApi;
//import com.infinities.keystone4j.token.TokenDriver;
//import com.infinities.keystone4j.trust.TrustApi;
//import com.infinities.keystone4j.utils.Cms;
//
//public class DeleteTokenCommand extends AbstractTokenCommand<Token> {
//
//	private final String tokenid;
//	private final static Logger logger = LoggerFactory.getLogger(DeleteTokenCommand.class);
//
//
//	public DeleteTokenCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, String tokenid) {
//		super(tokenApi, trustApi, tokenDriver);
//		this.tokenid = tokenid;
//	}
//
//	@Override
//	public Token execute() {
//		String uniqueid = null;
//		try {
//			uniqueid = Cms.Instance.hashToken(tokenid, null);
//		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | DecoderException e) {
//			logger.error("unexpected error", e);
//			throw Exceptions.UnexpectedException.getInstance(null);
//		}
//		this.getTokenDriver().deleteToken(uniqueid);
//		return null;
//	}
//
// }
