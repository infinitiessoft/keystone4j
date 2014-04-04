package com.infinities.keystone4j.trust.command;

import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

public class DeleteTrustCommand extends AbstractTrustCommand<Trust> {

	private final String trustid;


	public DeleteTrustCommand(TrustDriver trustDriver, String trustid) {
		super(trustDriver);
		this.trustid = trustid;
	}

	@Override
	public Trust execute() {
		this.getTrustDriver().deleteTrust(trustid);
		return null;
	}

}
