package com.infinities.keystone4j.admin.v3;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.token.provider.TokenProviderApi;

public class MockTokenProviderApiFactory implements Factory<TokenProviderApi> {

	private final TokenProviderApi tokenProviderApi;


	public MockTokenProviderApiFactory(TokenProviderApi tokenProviderApi) {
		this.tokenProviderApi = tokenProviderApi;
	}

	@Override
	public void dispose(TokenProviderApi arg0) {

	}

	@Override
	public TokenProviderApi provide() {
		return tokenProviderApi;
	}

}
