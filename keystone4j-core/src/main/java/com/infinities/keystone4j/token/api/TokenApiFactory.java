package com.infinities.keystone4j.token.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.token.TokenApi;
import com.infinities.keystone4j.token.TokenDriver;
import com.infinities.keystone4j.trust.TrustApi;

public class TokenApiFactory implements Factory<TokenApi> {

	private final TrustApi trustApi;
	private final TokenDriver tokenDriver;


	@Inject
	public TokenApiFactory(TrustApi trustApi, TokenDriver tokenDriver) {
		this.trustApi = trustApi;
		this.tokenDriver = tokenDriver;
	}

	@Override
	public void dispose(TokenApi arg0) {

	}

	@Override
	public TokenApi provide() {
		TokenApi tokenApi = new TokenApiImpl(trustApi, tokenDriver);
		return tokenApi;
	}

}
