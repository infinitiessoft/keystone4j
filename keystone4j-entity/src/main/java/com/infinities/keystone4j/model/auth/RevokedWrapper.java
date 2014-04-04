package com.infinities.keystone4j.model.auth;

import java.io.Serializable;
import java.util.List;

import com.infinities.keystone4j.model.token.Token;

public class RevokedWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1186349140617298738L;
	private List<Token> revoked;


	public RevokedWrapper() {

	}

	public RevokedWrapper(List<Token> revoked) {
		this.revoked = revoked;
	}

	public List<Token> getRevoked() {
		return revoked;
	}

	public void setRevoked(List<Token> revoked) {
		this.revoked = revoked;
	}

}
