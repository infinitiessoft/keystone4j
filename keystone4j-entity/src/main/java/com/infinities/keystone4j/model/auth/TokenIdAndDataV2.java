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
package com.infinities.keystone4j.model.auth;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.token.v2.wrapper.TokenV2DataWrapper;

public class TokenIdAndDataV2 implements MemberWrapper<TokenV2DataWrapper> {

	@XmlElement(name = "token_id")
	private String tokenid;
	@XmlElement(name = "token_data")
	private TokenV2DataWrapper tokenData;


	public TokenIdAndDataV2() {

	}

	public TokenIdAndDataV2(String tokenid, TokenV2DataWrapper tokenData) {
		super();
		this.tokenid = tokenid;
		this.tokenData = tokenData;
	}

	public String getTokenid() {
		return tokenid;
	}

	public void setTokenid(String tokenid) {
		this.tokenid = tokenid;
	}

	public TokenV2DataWrapper getTokenData() {
		return tokenData;
	}

	public void setTokenData(TokenV2DataWrapper tokenData) {
		this.tokenData = tokenData;
	}

	@Override
	public void setRef(TokenV2DataWrapper ref) {
		this.tokenData = ref;
	}

	@Override
	public TokenV2DataWrapper getRef() {
		return tokenData;
	}

}
