package com.infinities.keystone4j.token.model;

import org.codehaus.jackson.map.annotate.JsonView;

import com.infinities.keystone4j.Views;

public class TokenDataWrapper {

	@JsonView(Views.AuthenticateForToken.class)
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
