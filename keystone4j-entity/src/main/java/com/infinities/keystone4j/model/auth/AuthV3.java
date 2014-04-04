package com.infinities.keystone4j.model.auth;

public class AuthV3 {

	private Identity identity;
	private Scope scope;


	public AuthV3() {

	}

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

}
