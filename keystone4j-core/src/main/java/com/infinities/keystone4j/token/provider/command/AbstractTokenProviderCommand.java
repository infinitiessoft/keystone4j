package com.infinities.keystone4j.token.provider.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public abstract class AbstractTokenProviderCommand<T> implements Command<T> {

	private final TokenProviderApi tokenProviderApi;
	private final TokenProviderDriver tokenProviderDriver;


	public AbstractTokenProviderCommand(TokenProviderApi tokenProviderApi, TokenProviderDriver tokenProviderDriver) {
		super();
		this.tokenProviderApi = tokenProviderApi;
		this.tokenProviderDriver = tokenProviderDriver;
	}

	public TokenProviderApi getTokenProviderApi() {
		return tokenProviderApi;
	}

	public TokenProviderDriver getTokenProviderDriver() {
		return tokenProviderDriver;
	}

}
