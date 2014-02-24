package com.infinities.keystone4j.trust.api;

import javax.inject.Inject;

import org.glassfish.hk2.api.Factory;

import com.infinities.keystone4j.trust.TrustApi;
import com.infinities.keystone4j.trust.TrustDriver;

public class TrustApiFactory implements Factory<TrustApi> {

	private final TrustDriver trustDriver;


	@Inject
	public TrustApiFactory(TrustDriver trustDriver) {
		this.trustDriver = trustDriver;
	}

	@Override
	public void dispose(TrustApi arg0) {

	}

	@Override
	public TrustApi provide() {
		TrustApi trustApi = new TrustApiImpl(trustDriver);
		return trustApi;
	}

}
