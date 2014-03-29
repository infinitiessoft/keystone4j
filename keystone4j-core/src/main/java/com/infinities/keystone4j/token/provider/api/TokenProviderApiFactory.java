package com.infinities.keystone4j.token.provider.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.token.provider.TokenProviderApi;
import com.infinities.keystone4j.token.provider.TokenProviderDriver;

public class TokenProviderApiFactory implements Factory<TokenProviderApi> {

	private final TokenProviderDriver tokenProviderDriver;


	@Inject
	public TokenProviderApiFactory(TokenProviderDriver tokenProviderDriver) {
		this.tokenProviderDriver = tokenProviderDriver;
	}

	@Override
	public void dispose(TokenProviderApi arg0) {

	}

	@Override
	public TokenProviderApi provide() {
		TokenProviderApi tokenProviderApi = new TokenProviderApiImpl(tokenProviderDriver);
		return tokenProviderApi;
	}

}
