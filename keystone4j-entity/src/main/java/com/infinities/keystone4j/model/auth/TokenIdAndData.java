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
import com.infinities.keystone4j.model.token.wrapper.TokenDataWrapper;

public class TokenIdAndData implements MemberWrapper<TokenDataWrapper> {

	@XmlElement(name = "token_id")
	private String tokenid;
	@XmlElement(name = "token_data")
	private TokenDataWrapper tokenData;


	public TokenIdAndData() {

	}

	public TokenIdAndData(String tokenid, TokenDataWrapper tokenData) {
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

	public TokenDataWrapper getTokenData() {
		return tokenData;
	}

	public void setTokenData(TokenDataWrapper tokenData) {
		this.tokenData = tokenData;
	}

	@Override
	public void setRef(TokenDataWrapper ref) {
		this.tokenData = ref;
	}

	@Override
	public TokenDataWrapper getRef() {
		return tokenData;
	}

}
