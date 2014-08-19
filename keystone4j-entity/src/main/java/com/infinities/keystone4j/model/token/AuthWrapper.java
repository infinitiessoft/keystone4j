package com.infinities.keystone4j.model.token;

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
