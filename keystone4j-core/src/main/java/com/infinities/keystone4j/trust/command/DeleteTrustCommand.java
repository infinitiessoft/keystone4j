package com.infinities.keystone4j.trust.command;

import com.infinities.keystone4j.trust.TrustDriver;
import com.infinities.keystone4j.trust.model.Trust;

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
