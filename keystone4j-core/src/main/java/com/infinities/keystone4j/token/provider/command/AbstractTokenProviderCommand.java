package com.infinities.keystone4j.token.provider.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public abstract class AbstractTokenProviderCommand<T> implements Command<T> {

	private final TokenApi tokenApi;
	private final TokenProviderApi tokenProviderApi;
	private final TokenProviderDriver tokenProviderDriver;


	public AbstractTokenProviderCommand(TokenApi tokenApi, TokenProviderApi tokenProviderApi,
			TokenProviderDriver tokenProviderDriver) {
		super();
		this.tokenApi = tokenApi;
		this.tokenProviderApi = tokenProviderApi;
		this.tokenProviderDriver = tokenProviderDriver;
	}

	public TokenApi getTokenApi() {
		return tokenApi;
	}

	public TokenProviderApi getTokenProviderApi() {
		return tokenProviderApi;
	}

	public TokenProviderDriver getTokenProviderDriver() {
		return tokenProviderDriver;
	}

}
