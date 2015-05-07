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
//package com.infinities.keystone4j.token.provider.api.command;
//
//import com.infinities.keystone4j.model.token.v2.TokenV2DataWrapper;
//import com.infinities.keystone4j.token.provider.TokenProviderApi;
//import com.infinities.keystone4j.token.provider.TokenProviderDriver;
//
//public class CheckV2TokenCommand extends AbstractTokenProviderCommand<TokenV2DataWrapper> {
//
//	private final String tokenid;
//	private final String belongsTo;
//
//
//	// private final static Logger logger =
//	// LoggerFactory.getLogger(CheckV2TokenCommand.class);
//
//	public CheckV2TokenCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver, String tokenid,
//			String belongsTo) {
//		super(tokenProviderApi, tokenProviderDriver);
//		this.tokenid = tokenid;
//		this.belongsTo = belongsTo;
//	}
//
//	@Override
//	public TokenV2DataWrapper execute() {
//		return this.getTokenProviderApi().validateV2Token(tokenid, belongsTo);
//	}
//
// }
