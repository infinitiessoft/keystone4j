package com.infinities.keystone4j.model.auth;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.model.MemberWrapper;
import com.infinities.keystone4j.model.token.TokenDataWrapper;

public class TokenIdAndData implements MemberWrapper<TokenDataWrapper> {

	@XmlElement(name = "token_id")
	private String tokenid;
	@XmlElement(name = "token_data")
	private TokenDataWrapper tokenData;


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

}
