package com.infinities.keystone4j.model.token.wrapper;

import com.infinities.keystone4j.model.token.Auth;

public class AuthWrapper {

	private Auth auth;


	public AuthWrapper() {

	}

	public AuthWrapper(Auth auth) {
		this.auth = auth;

	}

	public Auth getAuth() {
		return auth;
	}

	public void setAuth(Auth auth) {
		this.auth = auth;
	}

}
