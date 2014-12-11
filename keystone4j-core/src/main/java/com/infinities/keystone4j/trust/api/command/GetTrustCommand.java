package com.infinities.keystone4j.trust.api.command;

import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

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
