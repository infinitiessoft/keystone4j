package com.infinities.keystone4j.token.api.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.trust.TrustApi;

public abstract class AbstractTokenCommand<T> implements Command<T> {

	private final TrustApi trustApi;
	private final TokenApi tokenApi;
	private final TokenDriver tokenDriver;


	public AbstractTokenCommand(TokenApi tokenApi, TrustApi trustApi, TokenDriver tokenDriver) {
		super();
		this.tokenApi = tokenApi;
		this.trustApi = trustApi;
		this.tokenDriver = tokenDriver;
	}

	public TrustApi getTrustApi() {
		return trustApi;
	}

	public TokenDriver getTokenDriver() {
		return tokenDriver;
	}

	public TokenApi getTokenApi() {
		return tokenApi;
	}

}
