package com.infinities.keystone4j.trust.command;

import com.infinities.keystone4j.Command;
import com.infinities.keystone4j.trust.TrustDriver;

public abstract class AbstractTrustCommand<T> implements Command<T> {

	private final TrustDriver trustDriver;


	public AbstractTrustCommand(TrustDriver trustDriver) {
		super();
		this.trustDriver = trustDriver;
	}

	public TrustDriver getTrustDriver() {
		return trustDriver;
	}

}
