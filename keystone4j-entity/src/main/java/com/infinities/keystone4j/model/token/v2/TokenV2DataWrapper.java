package com.infinities.keystone4j.model.token.v2;

import com.infinities.keystone4j.model.token.IToken;

public class TokenV2DataWrapper implements IToken {

	private Access access;


	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}

}
