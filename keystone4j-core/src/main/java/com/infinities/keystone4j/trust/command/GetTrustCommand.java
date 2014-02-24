package com.infinities.keystone4j.trust.command;

import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.model.Trust;

public class GetTrustCommand extends AbstractTrustCommand<Trust> {

	private final String trustid;


	public GetTrustCommand(TrustDriver trustDriver, String trustid) {
		super(trustDriver);
		this.trustid = trustid;
	}

	@Override
	public Trust execute() {
		return this.getTrustDriver().getTrust(trustid);
	}

}
