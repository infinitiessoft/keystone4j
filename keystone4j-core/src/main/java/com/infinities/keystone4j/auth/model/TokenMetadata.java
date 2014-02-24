package com.infinities.keystone4j.auth.model;

import javax.xml.bind.annotation.XmlElement;

import com.infinities.keystone4j.token.model.TokenDataWrapper;

public class TokenMetadata {

	@XmlElement(name = "token_id")
	private String tokenid;
	@XmlElement(name = "token_data")
	private TokenDataWrapper tokenData;


	public TokenMetadata(String tokenid, TokenDataWrapper tokenData) {
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

}
