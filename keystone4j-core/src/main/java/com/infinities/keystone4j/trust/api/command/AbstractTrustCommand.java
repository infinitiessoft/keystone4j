package com.infinities.keystone4j.trust.api.command;

import com.infinities.keystone4j.trust.TrustDriver;

public abstract class AbstractTrustCommand {

	private final TrustDriver trustDriver;


	public AbstractTrustCommand(TrustDriver trustDriver) {
		super();
		this.trustDriver = trustDriver;
	}

	public TrustDriver getTrustDriver() {
		return trustDriver;
	}

}
