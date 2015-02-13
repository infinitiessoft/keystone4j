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
