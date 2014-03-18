package com.infinities.keystone4j.mock;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.trust.TrustApi;

public class MockTrustApiFactory implements Factory<TrustApi> {

	private final TrustApi trustApi;


	public MockTrustApiFactory(TrustApi trustApi) {
		this.trustApi = trustApi;
	}

	@Override
	public void dispose(TrustApi arg0) {

	}

	@Override
	public TrustApi provide() {
		return trustApi;
	}

}
