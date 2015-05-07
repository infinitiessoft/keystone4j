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
//import com.infinities.keystone4j.model.trust.Trust;
//import com.infinities.keystone4j.token.TokenApi;
//import com.infinities.keystone4j.token.TokenDriver;
//import com.infinities.keystone4j.trust.TrustApi;
//
//public class DeleteTokensForUserCommand extends AbstractTokenCommand<List<Token>> {
//
//	private final String userid;
//	private final String projectid;
//
//
//	public DeleteTokensForUserCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver, String userid,
//			String projectid) {
//		super(tokenApi, trustApi, tokenDriver);
//		this.userid = userid;
//		this.projectid = projectid;
//	}
//
//	@Override
//	public List<Token> execute() {
//		this.getTokenDriver().deleteTokensForUser(userid, projectid);
//
//		List<Trust> trusts = this.getTrustApi().listTrustsForTrustee(userid);
//		for (Trust trust : trusts) {
//			this.getTokenDriver().deleteTokensForTrust(userid, trust.getId());
//		}
//		trusts = this.getTrustApi().listTrustsForTrustor(userid);
//		for (Trust trust : trusts) {
//			this.getTokenDriver().deleteTokensForTrust(trust.getTrustee().getId(), trust.getId());
//		}
//
//		return null;
//	}
// }
