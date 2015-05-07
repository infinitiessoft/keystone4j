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
//import com.infinities.keystone4j.Command;
//import com.infinities.keystone4j.token.TokenApi;
//import com.infinities.keystone4j.token.TokenDriver;
//import com.infinities.keystone4j.trust.TrustApi;
//
//public abstract class AbstractTokenCommand<T> implements Command<T> {
//
//	private final TrustApi trustApi;
//	private final TokenApi tokenApi;
//	private final TokenDriver tokenDriver;
//
//
//	public AbstractTokenCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver) {
//		super();
//		this.tokenApi = tokenApi;
//		this.trustApi = trustApi;
//		this.tokenDriver = tokenDriver;
//	}
//
//	public TrustApi getTrustApi() {
//		return trustApi;
//	}
//
//	public TokenDriver getTokenDriver() {
//		return tokenDriver;
//	}
//
//	public TokenApi getTokenApi() {
//		return tokenApi;
//	}
//
// }
