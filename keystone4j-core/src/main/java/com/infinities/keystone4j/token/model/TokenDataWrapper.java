package com.infinities.keystone4j.token.model;

public class TokenDataWrapper {

	private TokenData token;


	public TokenDataWrapper() {

	}

	public TokenDataWrapper(TokenData token) {
		this.token = token;
	}

	public TokenData getToken() {
		return token;
	}

	public void setToken(TokenData token) {
		this.token = token;
	}

}
