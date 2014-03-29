package com.infinities.keystone4j.auth.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthV3Wrapper {

	private AuthV3 auth;


	public AuthV3Wrapper() {

	}

	public AuthV3Wrapper(AuthV3 auth) {
		this.auth = auth;

	}

	public AuthV3 getAuth() {
		return auth;
	}

	public void setAuth(AuthV3 auth) {
		this.auth = auth;
	}

}
