package com.infinities.keystone4j.trust.api.command;

import com.infinities.keystone4j.NonTruncatedCommand;
import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.trust.TrustDriver;

public class ConsumeUseCommand extends AbstractTrustCommand implements NonTruncatedCommand<Trust> {

	private final String trustid;


	public ConsumeUseCommand(TrustDriver trustDriver, String trustid) {
		super(trustDriver);
		this.trustid = trustid;
	}

	@Override
	public Trust execute() throws Exception {
		this.getTrustDriver().consumeUse(trustid);
		return null;
	}

}
