package com.infinities.keystone4j.trust.api.command;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

public class GetTrustCommand extends AbstractTrustCommand implements NonTruncatedCommand<Trust> {

	private final String trustid;
	private final boolean deleted;


	public GetTrustCommand(TrustDriver trustDriver, String trustid, boolean deleted) {
		super(trustDriver);
		this.trustid = trustid;
		this.deleted = deleted;
	}

	@Override
	public Trust execute() {
		return this.getTrustDriver().getTrust(trustid, deleted);
	}

}
