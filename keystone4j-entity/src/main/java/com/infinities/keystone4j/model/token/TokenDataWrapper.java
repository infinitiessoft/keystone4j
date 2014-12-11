package com.infinities.keystone4j.model.token;

import com.fasterxml.jackson.annotation.JsonView;
import com.infinities.keystone4j.model.utils.Views;

public class TokenDataWrapper implements IToken {

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
