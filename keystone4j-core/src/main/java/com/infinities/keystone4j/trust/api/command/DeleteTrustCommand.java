package com.infinities.keystone4j.trust.api.command;

import com.infinities.keystone4j.model.trust.Trust;
import com.infinities.keystone4j.notification.NotifiableCommand;
import com.infinities.keystone4j.trust.TrustDriver;

public class DeleteTrustCommand extends AbstractTrustCommand implements NotifiableCommand<Trust> {

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

	@Override
	public Object getArgs(int index) {
		if (index == 1) {
			return trustid;
		}
		throw new IllegalArgumentException("invalid index");
	}

}
