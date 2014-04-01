package com.infinities.keystone4j.token.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.utils.jackson.Views;

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
