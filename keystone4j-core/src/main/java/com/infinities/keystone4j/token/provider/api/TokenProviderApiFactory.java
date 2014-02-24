package com.infinities.keystone4j.token.provider.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class TokenProviderApiFactory implements Factory<TokenProviderApi> {

	private final TokenApi tokenApi;
	private final TokenProviderDriver tokenProviderDriver;


	@Inject
	public TokenProviderApiFactory(TokenApi tokenApi, TokenProviderDriver tokenProviderDriver) {
		this.tokenApi = tokenApi;
		this.tokenProviderDriver = tokenProviderDriver;
	}

	@Override
	public void dispose(TokenProviderApi arg0) {

	}

	@Override
	public TokenProviderApi provide() {
		TokenProviderApi tokenProviderApi = new TokenProviderApiImpl(tokenApi, tokenProviderDriver);
		return tokenProviderApi;
	}

}
