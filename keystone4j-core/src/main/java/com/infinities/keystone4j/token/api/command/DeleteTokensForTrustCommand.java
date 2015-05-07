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
//import java.util.List;
//
//import com.infinities.keystone4j.model.token.Token;
//import com.infinities.keystone4j.token.TokenApi;
//import com.infinities.keystone4j.token.TokenDriver;
//import com.infinities.keystone4j.trust.TrustApi;
//
//public class DeleteTokensForTrustCommand extends AbstractTokenCommand<List<Token>> {
//
//	private final String userid;
//	private final String trustid;
//
//
//	public DeleteTokensForTrustCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, String userid,
//			String trustid) {
//		super(tokenApi, trustApi, tokenDriver);
//		this.userid = userid;
//		this.trustid = trustid;
//	}
//
//	@Override
//	public List<Token> execute() {
//		// List<Token> tokens = this.getTokenDriver().listTokensForTrust(userid,
//		// trustid);
//		this.getTokenDriver().deleteTokensForTrust(userid, trustid);
//
//		// for (Token token : tokens) {
//		// String uniqueid = new Cms().uniqueid(token.getId());
//		// // invalidate individual token cache
//		// }
//
//		// invalidate_revocation_list
//		return null;
//	}
//
// }
